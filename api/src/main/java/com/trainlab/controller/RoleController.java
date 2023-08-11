package com.trainlab.controller;

import com.trainlab.dto.RoleCreateDto;
import com.trainlab.dto.RoleDto;
import com.trainlab.model.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RoleController {
    @Operation(
            summary = "Spring Data Create role",
            description = "Creates a new role",
            responses = {
                    @ApiResponse(
                            responseCode = "CREATED",
                            description = "Created a new role",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Role.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "BAD_REQUEST",
                            description = "Validation error"
                    )
            }
    )
    ResponseEntity<RoleDto> createRole(RoleCreateDto roleCreateDto);

    @Operation(
            summary = "Spring Data receiving all roles",
            description = "Receiving all roles without limitations",
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "All user roles have successfully loaded",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Role.class)))

                    ),
                    @ApiResponse(responseCode = "INTERNAL_SERVER_ERROR", description = "Internal Server Error")
            }
    )
    ResponseEntity<List<RoleDto>> findAll();

    @Operation(
            summary = "Spring Data Role searching by its id",
            description = "Receive role by id",
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "Role has loaded Successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Role.class))
                    ),
                    @ApiResponse(
                            responseCode = "NOT_FOUND",
                            description = "Role not found"
                    )
            }
    )
    ResponseEntity<RoleDto> findRoleById(Integer id);

    @Operation(
            summary = "Spring Data Role receiving by role name",
            description = "Receive role by role name",
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "Role has loaded Successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Role.class))
                    ),
                    @ApiResponse(
                            responseCode = "NOT_FOUND",
                            description = "Role not found"
                    )
            }
    )
    ResponseEntity<RoleDto> findRoleByRoleName(String roleName);

    @Operation(
            summary = "Spring Data Update role ",
            description = "Updating based user role on given id and request body",
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "User role has successfully updated",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Role.class))
                    ),
                    @ApiResponse(
                            responseCode = "BAD_REQUEST",
                            description = "Validation error"
                    )
            }
    )
    ResponseEntity<RoleDto> updateRole(Integer id, RoleDto roleDto);
}
