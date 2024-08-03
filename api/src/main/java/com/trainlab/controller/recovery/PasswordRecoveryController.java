package com.trainlab.controller.recovery;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.trainlab.dto.auth.AuthRequestDto;
import com.trainlab.dto.auth.AuthResponseDto;
import com.trainlab.dto.recovery.EmailRequestDto;
import com.trainlab.dto.recovery.RecoveryCodeDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;

public interface PasswordRecoveryController {

    @Operation(
            summary = "Reset the user's password when user forgot it",
            description = "Reseting user password by using user's email",
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "Password changed successfully",
                            content = @Content(mediaType = "text/plain")
                    ),
                    @ApiResponse(
                            responseCode = "NOT_FOUND",
                            description = "User not found"
                    ),
                    @ApiResponse(
                            responseCode = "BAD_REQUEST",
                            description = "Validation exception"
                    )
            }
    )
    ResponseEntity<String> resetPassword(@Valid @RequestBody EmailRequestDto emailRequestDto,
                                         BindingResult bindingResult);

    @Operation(
            summary = "Verify recovery code from user with code in DB",
            description = "Verifying recovery code by using user's email and code",
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "Verified successfully",
                            content = @Content(mediaType = "text/plain")
                    ),
                    @ApiResponse(
                            responseCode = "NOT_FOUND",
                            description = "User could not be found"
                    ),
                    @ApiResponse(
                            responseCode = "TOO_MANY_REQUESTS",
                            description = "Too many requests"
                    ),
                    @ApiResponse(
                            responseCode = "BAD_REQUEST",
                            description = "Validation exception"
                    )
            }
    )
    ResponseEntity<String> verifyCode(@Valid @RequestBody RecoveryCodeDto recoveryCodeDto,
                                      BindingResult bindingResult) throws JsonProcessingException;

    @Operation(
            summary = "Create a new password after verifying recovery code",
            description = "Creating a new password",
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "Created successfully"
                    ),
                    @ApiResponse(
                            responseCode = "NOT_FOUND",
                            description = "User couldn't be found"
                    ),
                    @ApiResponse(
                            responseCode = "BAD_REQUEST",
                            description = "Validation exception"
                    )
            }
    )
    ResponseEntity<AuthResponseDto> createNewPassword(@Valid @RequestBody AuthRequestDto authRequestDto,
                                                      BindingResult bindingResult);
}
