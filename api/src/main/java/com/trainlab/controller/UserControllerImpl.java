package com.trainlab.controller;

import com.trainlab.dto.UserCreateRequestDto;
import com.trainlab.dto.UserFindAllResponseDto;
import com.trainlab.dto.UserFindByIdResponseDto;
import com.trainlab.dto.UserUpdateRequestDto;
import com.trainlab.dto.UserUpdateResponseDto;
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
public class UserControllerImpl implements UserController{

    private final UserService userService;

    private final UserMapper userMapper;

    @Override
    @PostMapping("/register")
    public ResponseEntity<String> createUser(@Valid @RequestBody @Parameter(description = "User information", required = true)
                                                 UserCreateRequestDto userCreateRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            throw new ValidationException(errorMessage);
        }

        userService.create(userCreateRequestDto);

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
    public ResponseEntity<List<UserFindAllResponseDto>> getAllUsers() {
        List<User> users = userService.findAll();
        List<UserFindAllResponseDto> usersDto = users.stream()
                .map(userMapper::toFindAllDto)
                .toList();

        return ResponseEntity.ok(usersDto);
    }

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<UserUpdateResponseDto> updateUser(@PathVariable("id") Long id,
                                                            @Valid @RequestBody UserUpdateRequestDto userUpdateRequestDto,
                                                            BindingResult bindingResult,
                                                            @AuthenticationPrincipal UserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            throw new ValidationException(errorMessage);
        }

        User updatedUser = userService.update(userUpdateRequestDto, id, userDetails);

        UserUpdateResponseDto updatedUserUpdateResponseDto = userMapper.toDto(updatedUser);

        return ResponseEntity.ok(updatedUserUpdateResponseDto);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<UserFindByIdResponseDto> findUserById(@PathVariable Long id, @AuthenticationPrincipal UserDetails detailsService) {
        User user = userService.findById(id, detailsService);

        UserFindByIdResponseDto responseDto = userMapper.toFindByIdDto(user);

        return ResponseEntity.ok(responseDto);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<String> forgotPassword(@PathVariable("id") Long id,
                                                 @Valid @RequestBody UserUpdateRequestDto userUpdateRequestDto) {
        userService.changePassword(userUpdateRequestDto, id);
        return ResponseEntity.ok("You changed password");
    }
}
