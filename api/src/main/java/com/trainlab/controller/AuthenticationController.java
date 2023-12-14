package com.trainlab.controller;

import com.trainlab.dto.UserDto;
import com.trainlab.model.ClientData;
import com.trainlab.model.security.RefreshToken;
import com.trainlab.security.dto.AuthRequestDto;
import com.trainlab.security.dto.AuthResponseDto;
import com.trainlab.security.TokenProvider;
import com.trainlab.model.security.AuthRefreshToken;
import com.trainlab.security.principal.UserPrincipal;
import com.trainlab.security.model.AccessToken;
import com.trainlab.service.AuthService;
import com.trainlab.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Tag(name = "AuthenticationController", description = "Authentication")
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final AuthService authService;

    // Проверить валидацию
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> loginUser(@RequestBody AuthRequestDto request) {
        String userEmail = request.getUserEmail();
        UserDto user = userService.findByEmail(userEmail);

        AccessToken token = tokenProvider.generate(new UserPrincipal(user.getId(), user.getRoles()));
        RefreshToken refreshToken = tokenProvider.generateRefreshToken();
        authService.createRefreshSession(request.getClientData(), user, refreshToken);

        return ResponseEntity.status(HttpStatus.OK).body(
                AuthResponseDto.builder()
                        .token(token)
                        .refreshToken(refreshToken)
                        .userDto(user)
                        .build()
        );
    }

    @PostMapping("/refresh-tokens")
    public ResponseEntity<AuthResponseDto> refreshToken(@Valid @RequestBody AuthRefreshToken authRefreshToken,
                                                        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            throw new ValidationException(errorMessage);
        }
        try {
            UserDto user = authService.validateAndRemoveRefreshToken(authRefreshToken);
            RefreshToken refreshToken = tokenProvider.generateRefreshToken();
            authService.createRefreshSession(
                    new ClientData(authRefreshToken.getFingerprint()), user, refreshToken
            );
            AccessToken accessToken = tokenProvider.generate(new UserPrincipal(user.getId(), user.getRoles()));
            return ResponseEntity.status(HttpStatus.OK).body(
                    AuthResponseDto.builder()
                            .token(accessToken)
                            .refreshToken(refreshToken)
                            .userDto(user)
                            .build()
            );
        } catch(Exception exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    AuthResponseDto.builder().build()
            );
        }
    }

    // Валидация authRefreshToken
    @PostMapping("/logout")
    public ResponseEntity<HttpStatus> logout(@RequestBody AuthRefreshToken authRefreshToken) {
        try {
            authService.deleteRefreshSession(authRefreshToken);
            return ResponseEntity.status(HttpStatus.OK).body(
                    HttpStatus.OK
            );
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    HttpStatus.UNAUTHORIZED
            );
        }
    }
}
