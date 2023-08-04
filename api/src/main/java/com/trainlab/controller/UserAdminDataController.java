package com.trainlab.controller;
import com.trainlab.model.AuthenticationInfo;
import com.trainlab.model.User;
import com.trainlab.repository.UserRepository;
import com.trainlab.security.config.JwtConfiguration;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class UserAdminDataController {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final JwtConfiguration configuration;

    @PutMapping("/passwords")
    public ResponseEntity<Object> updateUsersPasswords() {

        List<User> all = userRepository.findAll();

        for (User user : all) {
            AuthenticationInfo authenticationInfo = user.getAuthenticationInfo();

            String password = authenticationInfo.getUserPassword() + configuration.getPasswordSalt();
            String encodedPassword = encoder.encode(password);

            authenticationInfo.setUserPassword(encodedPassword);
            user.setAuthenticationInfo(authenticationInfo);
            userRepository.save(user);
        }

        return new ResponseEntity<>(all.size(), HttpStatus.OK);
    }

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
    //@Secured("ROLE_ADMIN")
    @PutMapping("/{id}/status")
    public String changeStatusDelete(
            @Parameter(description = "User ID") @PathVariable("id") Long id,
            @Parameter(description = "Status value") @RequestParam("isDeleted") boolean isDeleted) {

        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setDeleted(isDeleted);
            userRepository.save(user);
            return "Status changed successfully";
        } else {
            throw new EntityNotFoundException("User with id " + id + " not found!");
        }
    }
}