package com.trainlab.controller;

import com.trainlab.dto.UserCreateDto;
import com.trainlab.dto.UserDto;
import com.trainlab.dto.UserUpdateDto;
import com.trainlab.exception.ValidationException;
import com.trainlab.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    @PostMapping("/register")
    public ResponseEntity<String> createUser(@Valid @RequestBody @Parameter(description = "User information", required = true)
                                             UserCreateDto userCreateDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            throw new ValidationException(errorMessage);
        }

        userService.create(userCreateDto);

        String message = "Registration initiated. Please check your email for further instructions.";

        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @Override
    @GetMapping("/complete-registration")
    public ResponseEntity<String> completeRegistration(@RequestParam("userEmail") String userEmail) {
        try {
            userService.activateUser(userEmail);
            return ResponseEntity.status(HttpStatus.OK).body("Registration completed successfully!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error occurred during registration completion: " + e.getMessage());
        }
    }

    @Override
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long id,
                                              @Valid @RequestBody UserUpdateDto userUpdateDto,
                                              BindingResult bindingResult,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            throw new ValidationException(errorMessage);
        }
        return ResponseEntity.ok(userService.update(userUpdateDto, id, userDetails));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findUserById(@PathVariable Long id, @AuthenticationPrincipal UserDetails detailsService) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAuthorizedUser(id, detailsService));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<String> changePassword(@PathVariable("id") Long id,
                                                 @Valid @RequestBody UserUpdateDto userUpdateDto) {
        userService.changePassword(id, userUpdateDto);
        return ResponseEntity.status(HttpStatus.OK).body("You changed password");
    }
}
