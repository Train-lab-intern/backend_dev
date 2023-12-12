package com.trainlab.controller;

import com.trainlab.dto.UserDto;
import com.trainlab.security.dto.AuthRequestDto;
import com.trainlab.security.dto.AuthResponseDto;
import com.trainlab.security.TokenProvider;
import com.trainlab.security.principal.AccountPrincipal;
import com.trainlab.security.principal.UserPrincipal;
import com.trainlab.security.security.AccessToken;
import com.trainlab.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "AuthenticationController", description = "Authentication")
@RequestMapping("/api/v1")
public class AuthenticationController {
    private final TokenProvider tokenProvider;
    private final UserService userService;

    @PostMapping("/auth")
    public ResponseEntity<AuthResponseDto> loginUser(@RequestBody AuthRequestDto request) {
        String userEmail = request.getUserEmail();
        UserDto user = userService.findByEmail(userEmail);

        AccountPrincipal principal = new UserPrincipal(user.getId(), user.getRoles());
        AccessToken token = tokenProvider.generate(principal);

        return ResponseEntity.status(HttpStatus.OK).body(
                AuthResponseDto.builder()
                        .token(token)
                        .userDto(user)
                        .build()
        );
    }
}
