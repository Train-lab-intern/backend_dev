package com.trainlab.controller;

import com.trainlab.dto.UserDto;
import com.trainlab.dto.UserPageDto;
import com.trainlab.dto.UserPageUpdateDto;
import com.trainlab.dto.UserUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "UserController", description = "User management methods")
public interface UserController {
    @Operation(
            summary = "Find all users",
            description = "Find all users without limitations",
            responses =
                    @ApiResponse(
                        responseCode = "OK",
                        description = "Successfully loaded users",
                        content = @Content(
                                mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))
                    )
    )
    ResponseEntity<List<UserDto>> getAllUsers();

    @Operation(
            summary = "User search by user id",
            description = "Receive user by id",
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "Successfully loaded user",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))
                    ),
                    @ApiResponse(
                            responseCode = "NOT_FOUND",
                            description = "User not found"
                    )
            }
    )
    ResponseEntity<UserDto> findUserById(@PathVariable Long id);

    @Operation(
            summary = "Get user page by user id",
            description = "Receive user page by id",
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "Successfully loaded user",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = UserPageDto.class)))
                    ),
                    @ApiResponse(
                            responseCode = "NOT_FOUND",
                            description = "User not found"
                    )
            }
    )
    ResponseEntity<UserPageDto> userPageById(@PathVariable Long id);

    /*    @Operation(
            summary = "Complete Registration",
            description = "Completes the user registration process",
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "Registration completed successfully"
                    ),
                    @ApiResponse(
                            responseCode = "BAD_REQUEST",
                            description = "Error occurred during registration completion"
                    )
            }
    )
    ResponseEntity<String> completeRegistration(@RequestParam("userEmail") String userEmail);*/

    @Operation(
            summary = "Spring Data Update User",
            description = "Update User based on given id and request body",
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "Successfully updated User",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = UserPageUpdateDto.class)))
                    ),
                    @ApiResponse(
                            responseCode = "BAD_REQUEST",
                            description = "Validation error"
                    )
            }
    )
    ResponseEntity<UserPageDto> updateUserPage(@PathVariable("id") Long id,
                                               @Valid @RequestBody UserPageUpdateDto userUpdateDto,
                                               BindingResult bindingResult);


    @Operation(
            summary = "Changing the user's password when user forgot it",
            description = "Changing user password by using user's Id",
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "Password changed successfully",
                            content = @Content(mediaType = "text/plain")
                    ),
                    @ApiResponse(
                            responseCode = "NOT_FOUND",
                            description = "User not found"
                    )
            }
    )
    ResponseEntity<String> changePassword(@Valid @RequestBody UserUpdateDto userUpdateDto);
}