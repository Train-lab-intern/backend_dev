package com.trainlab.controller;

import com.trainlab.dto.UserCreateDto;
import com.trainlab.dto.UserDto;
import com.trainlab.dto.UserUpdateDto;
import com.trainlab.exception.ValidationException;
import com.trainlab.mapper.UserMapper;
import com.trainlab.model.User;
import com.trainlab.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    private final UserMapper userMapper;

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

        return ResponseEntity.ok(message);
    }

    @Override
    @GetMapping("/complete-registration")
    public ResponseEntity<String> completeRegistration(@RequestParam("userEmail") String userEmail) {
        try {
            userService.activateUser(userEmail);
            return ResponseEntity.ok("Registration completed successfully!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error occurred during registration completion: " + e.getMessage());
        }
    }

    @Override
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<User> users = userService.findAll();

        List<UserDto> usersDto = users.stream()
                .map(userMapper::toDto)
                .toList();

        return ResponseEntity.ok(usersDto);
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

        UserDto userDto = userMapper.toDto(userService.update(userUpdateDto, id, userDetails));
        return ResponseEntity.ok(userDto);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findUserById(@PathVariable Long id, @AuthenticationPrincipal UserDetails detailsService) {
        User user = userService.findById(id, detailsService);

        UserDto responseDto = userMapper.toDto(user);

        return ResponseEntity.ok(responseDto);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<String> forgotPassword(@PathVariable("id") Long id,
                                                 @Valid @RequestBody UserUpdateDto userUpdateDto) {
        userService.changePassword(userUpdateDto, id);

        return ResponseEntity.ok("You changed password");
    }
}
