package com.trainlab.controller;

import com.trainlab.dto.UserDto;
import com.trainlab.dto.UserUpdateDto;
import com.trainlab.exception.*;
import com.trainlab.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {
    private final UserService userService;

    @Override
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findUserById(@PathVariable Long id) {
        UserDto userDto = userService.findAuthorizedUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

/*    @Override
    @GetMapping("/complete-registration")
    public ResponseEntity<String> completeRegistration(@RequestParam("userEmail") String userEmail) {
        try {
            userService.activateUser(userEmail);
            return ResponseEntity.status(HttpStatus.OK).body("Registration completed successfully!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error occurred during registration completion: " + e.getMessage());
        }
    }*/

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long id,
                                              @Valid @RequestBody UserUpdateDto userUpdateDto,
                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            throw new ValidationException(errorMessage);
        }
        return ResponseEntity.ok(userService.update(userUpdateDto, id));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<String> changePassword(@PathVariable("id") Long id,
                                                 @Valid @RequestBody UserUpdateDto userUpdateDto) {
        userService.changePassword(id, userUpdateDto);
        return ResponseEntity.status(HttpStatus.OK).body("You changed password");
    }
}
