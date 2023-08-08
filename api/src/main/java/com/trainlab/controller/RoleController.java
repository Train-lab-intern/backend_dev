package com.trainlab.controller;

import com.trainlab.dto.request.RoleRequest;
import com.trainlab.model.Role;
import com.trainlab.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @Operation(
            summary = "Spring Data Create role",
            description = "Creates a new role",
            responses = {
                    @ApiResponse(
                            responseCode = "CREATED",
                            description = "Creating a new role",
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
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @PostMapping("/create")
    public ResponseEntity<Role> createRole(@Valid @RequestBody @Parameter(description = "Role data", required = true) RoleRequest roleRequest) {
        Role createdRole = roleService.create(roleRequest);
        return new ResponseEntity<>(createdRole, HttpStatus.CREATED);
    }

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
    @GetMapping
    public ResponseEntity<List<Role>> receiveAll() {
        List<Role> roles = roleService.findAll();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

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
    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable("id") Integer id,
                                           @Valid @RequestBody RoleRequest roleRequest) {
        Role updatedRole = roleService.update(roleRequest, id);
        return new ResponseEntity<>(updatedRole, HttpStatus.OK);
    }

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
    @GetMapping("/{id}")
    public ResponseEntity<Role> receiveRoleById(@PathVariable Integer id) {
        Role role = roleService.receiveById(id);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @Operation(
            summary = "Spring Data Role receiving by role name",
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
    @GetMapping("/receive")
    public ResponseEntity<Role> receiveRoleByRoleName(@Valid @RequestBody RoleRequest roleRequest) {
        Role role = roleService.receiveByRoleName(roleRequest.getRoleName());
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

}
