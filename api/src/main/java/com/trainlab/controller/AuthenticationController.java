package com.trainlab.controller;

import com.trainlab.configuration.JwtConfiguration;
import com.trainlab.dto.AuthRequestDto;
import com.trainlab.dto.AuthResponseDto;
import com.trainlab.dto.UserDto;
import com.trainlab.exception.ActivationException;
import com.trainlab.jwt.TokenProvider;
import com.trainlab.mapper.UserMapper;
import com.trainlab.model.User;
import com.trainlab.service.CustomUserDetailsService;
import com.trainlab.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "AuthenticationController", description = "Authentication")
@RequestMapping("/api/v1")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final TokenProvider tokenProvider;

    private final JwtConfiguration jwtConfiguration;

    private final UserService userService;

    private final CustomUserDetailsService userDetailsService;

    private final UserMapper userMapper;

    @PostMapping("/auth")
    public ResponseEntity<AuthResponseDto> loginUser(@RequestBody AuthRequestDto request) {
        String userEmail = request.getUserEmail();
        User userByEmail = userService.findByEmail(userEmail);

        if (!(userByEmail.getIsActive())) {
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

        UserDto userDto = userMapper.toDto(userByEmail);

        return ResponseEntity.status(HttpStatus.OK).body(
                AuthResponseDto.builder()
                        .userEmail(request.getUserEmail())
                        .token(token)
                        .userDto(userDto)
                        .build()
        );
    }
}
