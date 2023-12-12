package com.trainlab.controller;

import com.trainlab.dto.UserCreateDto;
import com.trainlab.dto.UserDto;
import com.trainlab.dto.UserUpdateDto;
import com.trainlab.exception.UsernameGenerationException;
import com.trainlab.exception.ValidationException;
import com.trainlab.security.TokenProvider;
import com.trainlab.security.dto.AuthResponseDto;
import com.trainlab.security.principal.AccountPrincipal;
import com.trainlab.security.principal.UserPrincipal;
import com.trainlab.security.security.AccessToken;
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

    private final TokenProvider tokenProvider;

    @Override
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> createUser(@Valid @RequestBody @Parameter(description = "User information", required = true)
                                             UserCreateDto userCreateDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            throw new ValidationException(errorMessage);
        }


        try {
            UserDto user = userService.create(userCreateDto);
            AccountPrincipal principal = new UserPrincipal(user.getId(), user.getRoles());
            AccessToken token = tokenProvider.generate(principal);
            return ResponseEntity.status(HttpStatus.OK).body(
                    AuthResponseDto.builder()
                            .token(token)
                            .userDto(user)
                            .build());
        } catch (UsernameGenerationException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    AuthResponseDto.builder().build()
            );
        }
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
    public ResponseEntity<UserDto> findUserById(@PathVariable Long id, @AuthenticationPrincipal  UserDetails detailsService) {
        if (detailsService == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        UserDto userDto = userService.findAuthorizedUser(id, detailsService);
        if (userDto != null)
            return ResponseEntity.status(HttpStatus.OK).body(userDto);
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<String> changePassword(@PathVariable("id") Long id,
                                                 @Valid @RequestBody UserUpdateDto userUpdateDto) {
        userService.changePassword(id, userUpdateDto);
        return ResponseEntity.status(HttpStatus.OK).body("You changed password");
    }
}
