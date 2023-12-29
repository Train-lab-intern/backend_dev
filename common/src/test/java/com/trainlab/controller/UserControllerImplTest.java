package com.trainlab.controller;

import com.trainlab.dto.UserCreateDto;
import com.trainlab.exception.MainExceptionHandler;
import com.trainlab.exception.UsernameGenerationException;
import com.trainlab.exception.ValidationException;
import com.trainlab.security.TokenProvider;
import com.trainlab.service.AuthService;
import com.trainlab.service.UserService;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    private MockMvc mockMvc;

    @BeforeAll
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @ParameterizedTest
    @MethodSource("paramsWithExceptions")
    void createUserShouldBeFailIfPasswordIsEmpty(UserCreateDto user, String exceptionMessage) throws Exception {
        MvcResult mvcResult = mockMvc.perform(request(POST, "/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(mvcResult.getResolvedException()).isInstanceOf(ValidationException.class);
        assertThat(Objects.requireNonNull(mvcResult.getResolvedException()).getMessage()).isEqualTo(exceptionMessage);

        verify(userService, never()).create(user);
        verify(tokenProvider, never()).generate(any());
        verify(tokenProvider, never()).generateRefreshToken();
        verify(authService, never()).createRefreshSession(any(), any());
    }

    static Stream<Arguments> paramsWithExceptions() {
        return Stream.of(
                Arguments.of(UserCreateDto.builder().email("vladthedevj6@gmail.com").password(" ").build(), "The password field is required"),
                Arguments.of(UserCreateDto.builder().email(" ").password("123456qW").build(), "The email field is required"),
                Arguments.of(UserCreateDto.builder().email(" ").password(" ").build(), "Email and password fields are required"),
                Arguments.of(UserCreateDto.builder().email("vladthedevj6@gmail.com").password("1234qW").build(), "User password must be between 8 and 256 characters"),
                Arguments.of(UserCreateDto.builder().email("vladthedevj6@gmail.com").password("987654321").build(), "Invalid login or password"),
                Arguments.of(UserCreateDto.builder().email("vladthedevj6@gmail.com").password("ABCDEFGH").build(), "Invalid login or password"),
                Arguments.of(UserCreateDto.builder().email("vladthedevj6@gmail.com").password("ывн244р4").build(), "Invalid login or password"),
                Arguments.arguments(UserCreateDto.builder().email("myemail@com").password("123456qW").build(), "Invalid login or password"),
                Arguments.arguments(UserCreateDto.builder().email("myemail%$^@domain").password("123456qW").build(), "Invalid login or password"),
                Arguments.arguments(UserCreateDto.builder().email("myemail@192.168.1.1").password("123456qW").build(), "Invalid login or password"),
                Arguments.arguments(UserCreateDto.builder().email(".myemail@1gmail.com").password("123456qW").build(), "Invalid login or password"),
                Arguments.of(UserCreateDto.builder()
                        .email("test.trainlab+Sun_of_the_sleepless_Melancholy_star_Whose_tearful_beam_glows_tremulously_far" +
                                "_That_showst_the_darkness_thou_canst_not_dispel_How_like_art_thou_to_joy_rememberd_well_" +
                                "What_is_this_life_if_ful_of_care_We_have_no_time_to_stand_and_stare1@gmail.com") // 257
                        .password("123456qW").build(), "User email must be between 1 and 256 characters"),
                Arguments.of(UserCreateDto.builder()
                        .email("vladthedevj6@gmail.com")
                        .password("Wdj0lLfsiBWp0vQ0CocM2BnD7ZkqTeiELahreGsJgCBXR88diCoa7tAOf0nFUSufkmxRTFSQsCevZnhuQnse" +
                                "9MCZ4d6K2fD3iDe0eRRv3Dn1r2RN21Q76k9AeHksffPn7fCjMgbSo3ApbxgH3VxWK7BMKYgFpUQ60efQwz96vI" +
                                "VoQGww0OBpJzloOKy44nb977HDrL2Bi439ScEWa7Nkohq5PI18si5OX9ISzV8S87qIBbfpZGRpapsW292ic7d5d").build(), //257
                        "User password must be between 8 and 256 characters")
        );
    }
}