package com.trainlab.controller;

import com.trainlab.dto.UserCreateDto;
import com.trainlab.dto.UserDto;
import com.trainlab.dto.UserUpdateDto;
import com.trainlab.security.dto.AuthResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "UserRestController", description = "User management methods")
public interface UserController {
    @Operation(
            summary = "Spring Data Create User",
            description = "Creates a new user",
            responses = {
                    @ApiResponse(
                            responseCode = "CREATED",
                            description = "User created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserCreateDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "BAD_REQUEST",
                            description = "Validation error"
                    )
            }
    )
    ResponseEntity<AuthResponseDto> createUser(@Valid @RequestBody @Parameter(description = "User information", required = true)
                                      UserCreateDto userCreateDto, BindingResult bindingResult);


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
            summary = "Spring Data User Find All Search",
            description = "Find All Users without limitations",
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "Successfully loaded Users",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))

                    ),
                    @ApiResponse(responseCode = "INTERNAL_SERVER_ERROR", description = "Internal Server Error")
            }
    )
    ResponseEntity<List<UserDto>> getAllUsers();

    @Operation(
            summary = "Spring Data Update User",
            description = "Update User based on given id and request body",
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "Successfully updated User",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = UserUpdateDto.class)))
                    ),
                    @ApiResponse(
                            responseCode = "BAD_REQUEST",
                            description = "Validation error"
                    )
            }
    )
    ResponseEntity<UserDto> updateUser(@PathVariable("id") Long id,
                                       @Valid @RequestBody UserUpdateDto userUpdateDto,
                                       BindingResult bindingResult,
                                       @AuthenticationPrincipal UserDetails userDetails);

    @Operation(
            summary = "Spring Data User Search by user Id",
            description = "Receive user by Id",
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "Successfully loaded User",
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
    ResponseEntity<UserDto> findUserById(@PathVariable Long id, @AuthenticationPrincipal UserDetails detailsService);

    @PatchMapping("/change-password/{id}")
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
    ResponseEntity<String> changePassword(@PathVariable("id") Long id,
                                          @Valid @RequestBody UserUpdateDto userUpdateDto);
}