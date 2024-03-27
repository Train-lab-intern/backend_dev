package com.trainlab.controller;

import com.trainlab.dto.UserCreateDto;
import com.trainlab.dto.UserDto;
import com.trainlab.dto.UserPageDto;
import com.trainlab.exception.LoginValidationException;
import com.trainlab.exception.ValidationException;
import com.trainlab.mapper.UserMapper;
import com.trainlab.model.User;
import com.trainlab.model.security.RefreshToken;
import com.trainlab.dto.AuthRequestDto;
import com.trainlab.security.dto.AuthResponseDto;
import com.trainlab.security.TokenProvider;
import com.trainlab.model.security.AuthRefreshToken;
import com.trainlab.principal.UserPrincipal;
import com.trainlab.security.model.AccessToken;
import com.trainlab.service.AuthService;
import com.trainlab.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class AuthenticationControllerImpl implements AuthenticationController {
    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final AuthService authService;
    private  final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> loginUser(@Valid @RequestBody AuthRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new LoginValidationException("Invalid login or password");

        User user =  userService.findUserByAuthenticationInfo(request);
        UserPageDto userDto = userMapper.toUserPageDto(user);
        AccessToken token = tokenProvider.generate(new UserPrincipal(userDto.getId(), userDto.getRoles()));
        RefreshToken refreshToken = tokenProvider.generateRefreshToken();
        authService.createRefreshSession(user, refreshToken);

        return ResponseEntity.status(HttpStatus.OK).body(
                AuthResponseDto.builder()
                        .token(token)
                        .refreshToken(refreshToken)
                        .userPageDto(userDto)
                        .build()
        );
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
        RefreshToken refreshToken = tokenProvider.generateRefreshToken();
        authService.createRefreshSession(user, refreshToken);
        AccessToken token = tokenProvider.generate(new UserPrincipal(user.getId(), userPageDto.getRoles()));

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    AuthResponseDto.builder()
                            .token(token)
                            .refreshToken(refreshToken)
                            .userPageDto(userMapper.toUserPageDto(user))
                            .build()
            );
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponseDto> refreshToken(@Valid @RequestBody AuthRefreshToken authRefreshToken,
                                                        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            throw new ValidationException(errorMessage);
        }
            UserPageDto user = authService.validateAndRemoveRefreshToken(authRefreshToken);
            RefreshToken refreshToken = tokenProvider.generateRefreshToken();
            authService.createRefreshSession(userMapper.toEntity(user), refreshToken);
            AccessToken accessToken = tokenProvider.generate(new UserPrincipal(user.getId(), user.getRoles()));
            return ResponseEntity.status(HttpStatus.OK).body(
                    AuthResponseDto.builder()
                            .token(accessToken)
                            .refreshToken(refreshToken)
                            .userPageDto(user)
                            .build()
            );
    }

    @PostMapping("/logout")
    public ResponseEntity<HttpStatus> logout(@Valid @RequestBody AuthRefreshToken authRefreshToken,
                                             BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            throw new ValidationException(errorMessage);
        }

        authService.deleteRefreshSession(authRefreshToken);
        SecurityContextHolder.clearContext();

        return ResponseEntity.status(HttpStatus.OK).body(
                HttpStatus.OK
        );
    }


}
