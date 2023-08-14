package com.trainlab.controller;

import com.trainlab.model.User;
import com.trainlab.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Tag(name = "AdminController", description = "Admin controller")
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;

    @Operation(
            summary = "Change User Status (for admins only)",
            description = "Changes the status (isDeleted) of a user by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "Status changed successfully"
                    ),
                    @ApiResponse(
                            responseCode = "NOT_FOUND",
                            description = "User not found"
                    )
            }
    )
    @PutMapping("/{id}/status")
    public ResponseEntity<String> changeStatusDelete(
            @Parameter(description = "User ID") @PathVariable("id") Long id,
            @Parameter(description = "Status value") @RequestParam("isDeleted") boolean isDeleted) {

        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setDeleted(isDeleted);
            userRepository.saveAndFlush(user);
            return ResponseEntity.status(HttpStatus.OK).body("Status changed successfully") ;
        } else {
            throw new EntityNotFoundException("User with id " + id + " not found!");
        }
    }
}