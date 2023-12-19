package com.trainlab.service.impl;

import com.trainlab.dto.UserCreateDto;
import com.trainlab.dto.UserDto;
import com.trainlab.exception.UsernameGenerationException;
import com.trainlab.mapper.UserMapper;
import com.trainlab.model.AuthenticationInfo;
import com.trainlab.model.Role;
import com.trainlab.model.User;
import com.trainlab.repository.RoleRepository;
import com.trainlab.repository.UserRepository;
import com.trainlab.service.EmailService;
import com.trainlab.util.PasswordEncode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @Mock
    private PasswordEncode passwordEncode;

    @Mock
    RoleRepository roleRepository;

    @Mock
    EmailService emailService;

    @Test
    void create() throws UsernameGenerationException {
         //  ReflectionTestUtils.setField(userService, "dbHost", "test.db");
        String email = "test@gmail.com";
        String userName = "testName";
        String password = "sdfkjgh376";
        UserCreateDto userRequest = UserCreateDto.builder()
                .email(email)
                //.username(userName)
                .password(password)
                .build();
        AuthenticationInfo authenticationInfo = AuthenticationInfo.builder()
                .email(email)
                .userPassword(password)
                .build();
        User expected = User.builder()
                .id(1L)
              //  .username(userName)
                .authenticationInfo(authenticationInfo)
                .created(Timestamp.valueOf("2023-07-13 16:28:08"))
                .changed(Timestamp.valueOf("2023-07-31 15:32:53"))
                .isActive(false)
                .isDeleted(false)
                .build();
        Role role = Role.builder()
                .id(1)
                .roleName("ROLE_USER")
                .created(Timestamp.valueOf("2023-07-13 16:28:08"))
                .changed(Timestamp.valueOf("2023-07-13 16:28:08"))
                .isDeleted(false)
                .build();
        String emailSubject = "Подтверждение регистрации";
        String message = """
                Спасибо за регистрацию! Пожалуйста, перейдите по ссылке ниже, чтобы завершить регистрацию:
                https://test.db/api/v1/users/complete-registration?userEmail=test%40gmail.com
                С наилучшими пожеланиями,
                Команда Trainlab""";
        when(userMapper.toEntity(userRequest)).thenReturn(expected);
        when(userRepository.saveAndFlush(any())).thenReturn(expected);
        when(passwordEncode.encodePassword(userRequest.getPassword())).thenReturn("");
        when(roleRepository.findByRoleName("ROLE_USER")).thenReturn(Optional.of(role));
        doNothing().when(emailService).sendRegistrationConfirmationEmail(userRequest.getEmail());
        User actual = userMapper.toEntity(userService.create(userRequest));
        assertEquals(expected, actual);
    }

}