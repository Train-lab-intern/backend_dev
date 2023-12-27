package com.trainlab.controller;

import com.trainlab.dto.UserCreateDto;
import com.trainlab.exception.UsernameGenerationException;
import com.trainlab.exception.ValidationException;
import com.trainlab.security.TokenProvider;
import com.trainlab.service.AuthService;
import com.trainlab.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(value = SpringExtension.class)
@SpringBootTest
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class UserControllerImplTest {

    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthService authService;

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private BindingResult bindingResult;

    @Test
    void createUserShouldBeFailIfEmailIsEmpty() throws UsernameGenerationException {
        UserCreateDto user = UserCreateDto.builder()
                .email(" ")
                .password("123456qW").build();

        Mockito.when(bindingResult.hasErrors()).thenReturn(true);
        FieldError fieldError = new FieldError("UserCreateDto", "email", "The email field is required.");
        Mockito.when(bindingResult.getFieldError()).thenReturn(fieldError);

        assertThrows(ValidationException.class, () -> userController.createUser(user, bindingResult));

        Mockito.verify(userService, Mockito.never()).create(user);
        Mockito.verify(tokenProvider, Mockito.never()).generate(Mockito.any());
        Mockito.verify(tokenProvider, Mockito.never()).generateRefreshToken();
        Mockito.verify(authService, Mockito.never()).createRefreshSession(Mockito.any(), Mockito.any());
    }

    @Test
    void createUserShouldBeFailIfPasswordIsEmpty() throws UsernameGenerationException {
        UserCreateDto user = UserCreateDto.builder()
                .email("vladthedevj6@gmail.com")
                .password(" ").build();

        Mockito.when(bindingResult.hasErrors()).thenReturn(true);
        FieldError fieldError = new FieldError("UserCreateDto", "password", "The password field is required.");
        Mockito.when(bindingResult.getFieldError()).thenReturn(fieldError);

        assertThrows(ValidationException.class, () -> userController.createUser(user, bindingResult));

        Mockito.verify(userService, Mockito.never()).create(user);
        Mockito.verify(tokenProvider, Mockito.never()).generate(Mockito.any());
        Mockito.verify(tokenProvider, Mockito.never()).generateRefreshToken();
        Mockito.verify(authService, Mockito.never()).createRefreshSession(Mockito.any(), Mockito.any());
    }

    @Test
    void createUserShouldBeFailIfPasswordAndEmailAreEmpty() throws UsernameGenerationException {
        UserCreateDto user = UserCreateDto.builder()
                .email(" ")
                .password(" ").build();

        Mockito.when(bindingResult.hasErrors()).thenReturn(true);
        FieldError emailErrorField = new FieldError("UserCreateDto", "email", "Email and password fields are required.");
        FieldError passwordEmailField = new FieldError("UserCreateDto", "password", "Email and password fields are required.");
        Mockito.when(bindingResult.getFieldError()).thenReturn(emailErrorField, passwordEmailField);

        assertThrows(ValidationException.class, () -> userController.createUser(user, bindingResult));

        Mockito.verify(userService, Mockito.never()).create(user);
        Mockito.verify(tokenProvider, Mockito.never()).generate(Mockito.any());
        Mockito.verify(tokenProvider, Mockito.never()).generateRefreshToken();
        Mockito.verify(authService, Mockito.never()).createRefreshSession(Mockito.any(), Mockito.any());
    }

    @Test
    void createUserShouldBeFailIfPasswordLessThan8Characters() throws UsernameGenerationException {
        UserCreateDto user = UserCreateDto.builder()
                .email("vladthedevj6@gmail.com")
                .password("1234qW").build();

        Mockito.when(bindingResult.hasErrors()).thenReturn(true);
        FieldError fieldError = new FieldError("UserCreateDto", "password", "User password must be between 8 and 256 characters");
        Mockito.when(bindingResult.getFieldError()).thenReturn(fieldError);

        assertThrows(ValidationException.class, () -> {
            userController.createUser(user, bindingResult);
        });

        Mockito.verify(userService, Mockito.never()).create(user);
        Mockito.verify(tokenProvider, Mockito.never()).generate(Mockito.any());
        Mockito.verify(tokenProvider, Mockito.never()).generateRefreshToken();
        Mockito.verify(authService, Mockito.never()).createRefreshSession(Mockito.any(), Mockito.any());
    }
}