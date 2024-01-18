package com.trainlab.service.impl;

import com.trainlab.dto.RoleDto;
import com.trainlab.dto.UserDto;
import com.trainlab.exception.RefreshTokenNotFoundException;
import com.trainlab.mapper.UserMapper;
import com.trainlab.model.AuthenticationInfo;
import com.trainlab.model.RefreshSessions;
import com.trainlab.model.Role;
import com.trainlab.model.User;
import com.trainlab.model.security.AuthRefreshToken;
import com.trainlab.model.security.RefreshToken;
import com.trainlab.repository.AuthRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AuthRepository authRepository;

    private UserDto userDto;

    private User user;

    private RefreshToken refreshToken;

    private RefreshSessions expectedRefreshSession;

    private AuthRefreshToken authRefreshToken;

    @BeforeAll
    void init() {
        userDto = UserDto.builder()
                .id(1L)
                .username("user-1")
                .email("vladthedevj6@gmail.com")
                .created(Timestamp.valueOf(LocalDateTime.now().withNano(0)))
                .changed(Timestamp.valueOf(LocalDateTime.now().withNano(0)))
                .roles(List.of(
                        RoleDto.builder()
                                .id(1)
                                .roleName("ROLE_USER")
                                .created(Timestamp.valueOf(LocalDateTime.now().withNano(0)))
                                .changed(Timestamp.valueOf(LocalDateTime.now().withNano(0))).build())
                ).build();
        AuthenticationInfo authenticationInfo = AuthenticationInfo.builder()
                .email("vladthedevj6@gmail.com")
                .userPassword("123456Qw")
                .build();
        user = User.builder()
                .id(1L)
                .username("user-1")
                .authenticationInfo(authenticationInfo)
                .created(Timestamp.valueOf(LocalDateTime.now().withNano(0)))
                .changed(Timestamp.valueOf(LocalDateTime.now().withNano(0)))
                .roles(List.of(
                        Role.builder()
                                .id(1)
                                .roleName("ROLE_USER")
                                .created(Timestamp.valueOf(LocalDateTime.now().withNano(0)))
                                .changed(Timestamp.valueOf(LocalDateTime.now().withNano(0))).build())
                ).build();
        refreshToken = RefreshToken.builder()
                .value(UUID.fromString("7aebf344-73df-4ce1-ad91-a1e2c6a4bcdf"))
                .issuedAt(Instant.now())
                .expiredAt(Instant.now().plus(Duration.ofDays(90L))).build();
        expectedRefreshSession = RefreshSessions.builder()
                .id(1L)
                .user(user)
                .refreshToken(refreshToken.getValue())
                .expiredAt(refreshToken.getExpiredAt())
                .issuedAt(refreshToken.getIssuedAt()).build();
    }

    @BeforeEach
    void setUp() {
        authRefreshToken = AuthRefreshToken.builder()
                .refreshToken("7aebf344-73df-4ce1-ad91-a1e2c6a4bcdf").build();
    }

    @Test
    void sessionShouldBeCreatedIfParametersValid() {
        when(userMapper.toEntity(userDto)).thenReturn(new User());
        when(authRepository.saveAndFlush(any(RefreshSessions.class))).thenReturn(expectedRefreshSession);

        authService.createRefreshSession(userDto, refreshToken);
        verify(userMapper).toEntity(userDto);
        verify(authRepository).saveAndFlush(expectedRefreshSession);
    }

/*    @Test
    void deleteRefreshSessionShouldBeSuccessIfRefreshSessionExist() {
        AuthRefreshToken authRefreshToken1 = AuthRefreshToken.builder()
                .refreshToken("7aedf644-73df-4ce1-ad91-a1e2c6a4bcdf")
                .build();

        doReturn(Optional.of(expectedRefreshSession))
                .when(authRepository).findByRefreshToken(UUID.fromString("7aedf644-73df-4ce1-ad91-a1e2c6a4bcdf"));

*//*        doNothing().when(authRepository).delete(any(RefreshSessions.class));*//*

        authService.deleteRefreshSession(authRefreshToken1);
        verify(authRepository, times(1)).findByRefreshToken(UUID.fromString(authRefreshToken1.getRefreshToken()));
*//*        verify(authRepository, times(1)).delete(any(RefreshSessions.class));*//*
    }*/

    @Test
    void deleteRefreshSessionShouldBeFailIfRefreshSessionNotExist() {
        when(authRepository.findByRefreshToken(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(RefreshTokenNotFoundException.class, () -> authService.deleteRefreshSession(authRefreshToken));
    }
}
