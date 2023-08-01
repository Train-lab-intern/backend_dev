package com.trainlab.security.controller;

import com.trainlab.exception.ActivationException;
import com.trainlab.exception.ObjectNotFoundException;
import com.trainlab.repository.UserRepository;
import com.trainlab.security.config.JwtConfiguration;
import com.trainlab.security.dto.AuthRequest;
import com.trainlab.security.dto.AuthResponse;
import com.trainlab.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final TokenProvider tokenProvider;

    private final UserDetailsService userDetailsService;

    private final JwtConfiguration jwtConfiguration;
    private final UserRepository userRepository;

    @PostMapping("/auth")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody AuthRequest request) {
        String userEmail = request.getUserEmail();
        if (!(userRepository.findByAuthenticationInfoEmail(userEmail)
                .orElseThrow(() -> new ObjectNotFoundException("This user doesn't exist")).isActive())) {
            throw new ActivationException("User not activated");
        }

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUserEmail(),
                        request.getUserPassword() + jwtConfiguration.getPasswordSalt()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        String token = tokenProvider.generateToken(userDetailsService.loadUserByUsername(request.getUserEmail()));

        return ResponseEntity.ok(
                AuthResponse.builder()
                        .userEmail(request.getUserEmail())
                        .token(token)
                        .build()
        );
    }
}
