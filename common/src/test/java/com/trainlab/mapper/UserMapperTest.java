package com.trainlab.mapper;

import com.trainlab.TestApplication;
import com.trainlab.dto.UserCreateDto;
import com.trainlab.dto.UserDto;
import com.trainlab.dto.UserUpdateDto;
import com.trainlab.model.AuthenticationInfo;
import com.trainlab.model.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {TestApplication.class, UserMapperImpl.class})
@RunWith(SpringRunner.class)
class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    void toEntity() {
        String email = "test@gmail.com";
        String userName = "testName";
        String password = "sdfkjgh376";

        UserCreateDto userCreateDto = UserCreateDto.builder()
                .email(email)
                .username(userName)
                .password(password)
                .build();
        AuthenticationInfo authenticationInfo = AuthenticationInfo.builder()
                .email(email)
                .userPassword(password)
                .build();
        User expected = User.builder()
                .username(userName)
                .authenticationInfo(authenticationInfo)
                .created(Timestamp.valueOf(java.time.LocalDateTime.now().withNano(0)))
                .changed(Timestamp.valueOf(java.time.LocalDateTime.now().withNano(0)))
                .build();

        User actual = userMapper.toEntity(userCreateDto);
        assertEquals(expected, actual);
    }

    @Test
    void partialUpdateToEntity() {
        String email = "test@gmail.com";
        String userName = "testName";
        String password = "sdfkjgh376";
        AuthenticationInfo authenticationInfo1 = AuthenticationInfo.builder()
                .email(email)
                .userPassword(password)
                .build();
        User user = User.builder()
                .username(userName)
                .authenticationInfo(authenticationInfo1)
                .created(Timestamp.valueOf(java.time.LocalDateTime.now().withNano(0)))
                .changed(Timestamp.valueOf(java.time.LocalDateTime.now().withNano(0)))
                .isActive(false)
                .isDeleted(false)
                .build();

        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .email(email)
                .username(userName)
                .password(password)
                .build();

        AuthenticationInfo authenticationInfo = AuthenticationInfo.builder()
                .email(email)
                .userPassword(password)
                .build();

        User expected = User.builder()
                .username(userName)
                .authenticationInfo(authenticationInfo)
                .created(Timestamp.valueOf(java.time.LocalDateTime.now().withNano(0)))
                .changed(Timestamp.valueOf(java.time.LocalDateTime.now().withNano(0)))
                .build();

        User actual = userMapper.partialUpdateToEntity(userUpdateDto, user);
        assertEquals(expected, actual);
    }


    @Test
    void toEntityFromUserDto() {
        Long id = 1L;
        String email = "test@gmail.com";
        String userName = "testName";
        UserDto userDto = UserDto.builder()
                .id(id)
                .username(userName)
                .email(email)
                .created(Timestamp.valueOf("2023-01-01 00:00:00"))
                .changed(Timestamp.valueOf("2023-01-02 00:00:00"))
                .active(false)
                .build();

        AuthenticationInfo authenticationInfo = AuthenticationInfo.builder()
                .email(email)
                .build();
        User expected = User.builder()
                .id(id)
                .username(userName)
                .authenticationInfo(authenticationInfo)
                .created(Timestamp.valueOf("2023-01-01 00:00:00"))
                .changed(Timestamp.valueOf("2023-01-02 00:00:00"))
                .build();

        User actual = userMapper.toEntity(userDto);
        assertEquals(expected, actual);
    }

    @Test
    void toDto() {
        Long id = 1L;
        String email = "test@gmail.com";
        String userName = "testName";
        String password = "sdfkjgh376";
        AuthenticationInfo authenticationInfo = AuthenticationInfo.builder()
                .email(email)
                .userPassword(password)
                .build();
        User user = User.builder()
                .id(id)
                .username(userName)
                .authenticationInfo(authenticationInfo)
                .created(Timestamp.valueOf("2023-01-01 00:00:00"))
                .changed(Timestamp.valueOf("2023-01-02 00:00:00"))
                .isActive(false)
                .isDeleted(false)
                .build();

        UserDto expected = UserDto.builder()
                .id(id)
                .email(email)
                .username(userName)
                .created(Timestamp.valueOf("2023-01-01 00:00:00"))
                .changed(Timestamp.valueOf("2023-01-02 00:00:00"))
                .active(false)
                .build();
        UserDto actual = userMapper.toDto(user);
        assertEquals(expected, actual);
    }
}
