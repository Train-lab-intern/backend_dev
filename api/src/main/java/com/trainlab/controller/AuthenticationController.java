package com.trainlab.controller;

import com.trainlab.dto.UserCreateDto;
import com.trainlab.model.security.AuthRefreshToken;
import com.trainlab.dto.AuthRequestDto;
import com.trainlab.security.dto.AuthResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthenticationController {

    @Operation(
        summary = "Login user by email and password.",
        description = "Get user data by email and password. The response is AuthResponse object with token, refresh token" +
                " and user object.",
        responses ={
                @ApiResponse(
                    responseCode = "CREATED",
                    description = "Login successful.",
                    content = @Content(schema = @Schema(implementation = AuthRequestDto.class),
                                       mediaType = "application/json")
                ),
                @ApiResponse(
                        responseCode = "BAD_REQUEST",
                        description = "Validation error."
                )}
    )
    ResponseEntity<AuthResponseDto> loginUser(@RequestBody AuthRequestDto request, BindingResult bindingResult);

    @Operation(
            summary = "Spring Data Create User",
            description = "Creates a new user",
            responses = {
                    @ApiResponse(
                            responseCode = "CREATED",
                            description = "User created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(allOf = {UserCreateDto.class})
                            )
                    ),
                    @ApiResponse(
                            responseCode = "BAD_REQUEST",
                            description = "Validation error"
                    ),
                    @ApiResponse(
                            responseCode = "INTERNAL_SERVER_ERROR",
                            description = "Username generation error"
                    )
            }
    )
    ResponseEntity<AuthResponseDto> createUser(@Valid @RequestBody @Parameter(description = "User information", required = true)
                                               UserCreateDto userCreateDto, BindingResult bindingResult);

    @Operation(
        summary = "Refresh old token.",
        description = "When the front verified jwt token and it expired. Client sends refresh token to this controller" +
                "and if refresh token is valid and has not expired, controller sends to the client new jwt and refresh token.",
        responses = {
                @ApiResponse(
                        responseCode = "CREATED",
                        description = "Refresh token successful.",
                        content = @Content(schema = @Schema(implementation = AuthRefreshToken.class),
                                           mediaType = "application/json")
                ),
                @ApiResponse(
                        responseCode = "BAD_REQUEST",
                        description = "Validation error."
                ),
                @ApiResponse(
                        responseCode = "UNAUTHORIZED",
                        description = "The RefreshToken doesn't exist or has expired."
                )}
    )
    ResponseEntity<AuthResponseDto> refreshToken(@Valid @RequestBody AuthRefreshToken authRefreshToken,
                                                 BindingResult bindingResult);


    @Operation(
        summary = "User logout.",
        description = "When a client sends a request to this controller and refresh token is valid, the refresh token" +
                    " will removed from the DB.",
        responses = {
                @ApiResponse(
                        responseCode = "OK",
                        description = "Logout successful.",
                        content = @Content(schema = @Schema(implementation = AuthRefreshToken.class),
                                           mediaType = "application/json")
                        ),
                @ApiResponse(
                        responseCode = "BAD_REQUEST",
                        description = "Validation error."
                )}
    )
    ResponseEntity<HttpStatus> logout(@Valid @RequestBody AuthRefreshToken authRefreshToken,
                                      BindingResult bindingResult);
}
