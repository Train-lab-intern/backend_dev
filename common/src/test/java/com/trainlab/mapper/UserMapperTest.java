package com.trainlab.mapper;

import com.trainlab.TestApplication;
import com.trainlab.dto.request.UserRequest;
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
    UserMapper userMapper;

    @Test
    void toEntity() {
        String email = "test@gmail.com";
        String userName = "testName";
        String password = "sdfkjgh376";
        UserRequest userRequest = UserRequest.builder()
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
        User actual = userMapper.toEntity(userRequest);
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
                .username(email)
                .authenticationInfo(authenticationInfo1)
                .created(Timestamp.valueOf(java.time.LocalDateTime.now().withNano(0)))
                .changed(Timestamp.valueOf(java.time.LocalDateTime.now().withNano(0)))
                .active(false)
                .isDeleted(false)
                .build();
        UserRequest userRequest = UserRequest.builder()
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
        User actual = userMapper.partialUpdateToEntity(userRequest, user);
        assertEquals(expected, actual);
    }
}