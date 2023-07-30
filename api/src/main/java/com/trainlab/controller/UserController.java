package com.trainlab.controller;

import com.trainlab.dto.request.UserRequest;
import com.trainlab.model.User;
import com.trainlab.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rest/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Spring Data Create User",
            description = "Creates a new user",
            responses = {
                    @ApiResponse(
                            responseCode = "CREATED",
                            description = "User created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = User.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "BAD_REQUEST",
                            description = "Validation error"
                    )
            }
    )
//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
//    @PostMapping
//    public ResponseEntity<User> createUser(@Valid @RequestBody @Parameter(description = "User information", required = true) UserRequest userRequest) {
//
//        User createdUser = userService.create(userRequest);
//        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
//    }
//}
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody @Parameter(description = "User information", required = true) UserRequest userRequest) {
        User createdUser = userService.create(userRequest);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @Operation(
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
    @GetMapping("/complete-registration")
    public ResponseEntity<String> completeRegistration(@RequestParam("userEmail") String userEmail) {
        try {
            userService.activateUser(userEmail);
            return ResponseEntity.ok("Registration completed successfully!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error occurred during registration completion: " + e.getMessage());
        }
    }
}
