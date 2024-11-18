package com.trainlab.api.controller;

import com.trainlab.api.util.ValidationUtil;
import com.trainlab.dto.*;
import com.trainlab.dto.auth.AuthRequestDto;
import com.trainlab.exception.LoginEmptyFieldsValidationException;
import com.trainlab.exception.LoginValidationException;
import com.trainlab.exception.ValidationException;
import com.trainlab.mapper.UserMapper;
import com.trainlab.model.User;
import com.trainlab.dto.auth.AuthResponseDto;
import com.trainlab.model.security.AuthRefreshToken;
import com.trainlab.service.AuthService;
import com.trainlab.service.UserService;
import com.trainlab.service.token.TokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Tag(name = "AuthenticationController", description = "Authentication")
@RequestMapping(value = "/api/v1/auth")
public class AuthenticationControllerImpl implements AuthenticationController {
    private final UserService userService;
    private final AuthService authService;
    private final UserMapper userMapper;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> loginUser(@Valid @RequestBody AuthRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            if (request.isFieldsBlank())
                throw new LoginEmptyFieldsValidationException("Email and password fields are required");
            if (request.getEmail().isBlank() || request.getPassword().isBlank()) {
                String error = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
                throw new LoginEmptyFieldsValidationException(error);
            }
            throw new LoginValidationException("Invalid login or password");
        }

        User user = userService.findUserByAuthenticationInfo(request);
        UserPageDto userPageDto = userMapper.toUserPageDto(user);

        return ResponseEntity.status(HttpStatus.OK).body(tokenService.generateTokensAndCreateSession(userPageDto));
    }

    @Override
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> createUser(@Valid @RequestBody UserCreateDto userCreateDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            if (userCreateDto.isFieldsBlank())
                throw new ValidationException("Email and password fields are required");

            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            throw new ValidationException(errorMessage);
        }

        User user = userService.create(userCreateDto);
        UserPageDto userPageDto = userMapper.toUserPageDto(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(tokenService.generateTokensAndCreateSession(userPageDto));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponseDto> refreshToken(@Valid @RequestBody AuthRefreshToken authRefreshToken,
                                                        BindingResult bindingResult) {

        ValidationUtil.isRequestValid(bindingResult);
        UserPageDto userPageDto = authService.validateAndRemoveRefreshToken(authRefreshToken);

        return ResponseEntity.status(HttpStatus.OK).body(tokenService.generateTokensAndCreateSession(userPageDto));
    }

    @PostMapping("/logout")
    public ResponseEntity<HttpStatus> logout(@Valid @RequestBody AuthRefreshToken authRefreshToken,
                                             BindingResult bindingResult) {

        ValidationUtil.isRequestValid(bindingResult);

        authService.deleteRefreshSession(authRefreshToken);
        SecurityContextHolder.clearContext();

        return ResponseEntity.status(HttpStatus.OK).body(
                HttpStatus.OK
        );
    }
}
