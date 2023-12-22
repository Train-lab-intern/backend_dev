package com.trainlab.controller;

import com.trainlab.dto.UserCreateDto;
import com.trainlab.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
@RunWith(value = MockitoJUnitRunner.class)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class UserControllerImplTest {

    @InjectMocks
    private UserControllerImpl userController;

    private MockMvc mockMvc;

    private UserCreateDto user;
    @BeforeAll
    void insertUser() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        UserCreateDto correctUser = UserCreateDto.builder()
                .email("trainlab@gmail.com")
                .password("123456qW").build();

        UserCreateDto userWithEmptyEmail = UserCreateDto.builder()
                .email(" ")
                .password("123456qW").build();

        UserCreateDto userWithEmptyPassword = UserCreateDto.builder()
                .email("trainlab@gmail.com")
                .password(" ").build();

        UserCreateDto userWithEmptyFields = UserCreateDto.builder()
                .email(" ")
                .password(" ").build();
    }

    @Test
    void userRegisterShouldThrowExceptionIfEmailIsEmpty() throws Exception {
        user = UserCreateDto.builder()
                .email(" ")
                .password("123456qW").build();

        mockMvc.perform(MockMvcRequestBuilders
                .request(HttpMethod.POST, "/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(user.toString()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

    }

}
