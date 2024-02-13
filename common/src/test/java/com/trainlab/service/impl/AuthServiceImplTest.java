package com.trainlab.service.impl;

import com.trainlab.dto.RoleDto;
import com.trainlab.dto.UserDto;
import com.trainlab.exception.RefreshTokenNotFoundException;
import com.trainlab.exception.TokenExpiredException;
import com.trainlab.mapper.UserMapper;
import com.trainlab.model.AuthenticationInfo;
import com.trainlab.model.RefreshSessions;
import com.trainlab.model.Role;
import com.trainlab.model.User;
import com.trainlab.model.security.AuthRefreshToken;
import com.trainlab.model.security.RefreshToken;
import com.trainlab.repository.AuthRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

    @BeforeEach
    void init() {
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
        authRefreshToken = AuthRefreshToken.builder()
                .refreshToken("7aebf344-73df-4ce1-ad91-a1e2c6a4bcdf").build();
        refreshToken = RefreshToken.builder()
                .value(UUID.fromString("7aebf344-73df-4ce1-ad91-a1e2c6a4bcdf"))
                .issuedAt(Instant.now())
                .expiredAt(Instant.now().plus(Duration.ofDays(90L))).build();
        userDto = UserDto.builder()
                .id(1L)
                .generatedName("user-1")
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
        expectedRefreshSession = RefreshSessions.builder()
                .id(1L)
                .user(user)
                .refreshToken(refreshToken.getValue())
                .expiredAt(refreshToken.getExpiredAt())
                .issuedAt(refreshToken.getIssuedAt()).build();
    }

    @Test
    void deleteRefreshSessionShouldBeFailIfRefreshSessionNotExist() {
        when(authRepository.findByRefreshToken(UUID.fromString(authRefreshToken.getRefreshToken())))
                .thenReturn(Optional.empty());

        assertThrows(RefreshTokenNotFoundException.class, () -> authService.deleteRefreshSession(authRefreshToken));

        verify(authRepository, times(1)).findByRefreshToken(UUID.fromString(authRefreshToken.getRefreshToken()));
        verify(authRepository, never()).delete(any(RefreshSessions.class));
    }

    @Test
    void deleteRefreshSessionShouldBeSuccessIfRefreshSessionExist() {
        doReturn(Optional.of(expectedRefreshSession))
                .when(authRepository).findByRefreshToken(UUID.fromString(authRefreshToken.getRefreshToken()));

        doNothing().when(authRepository).delete(any(RefreshSessions.class));

        authService.deleteRefreshSession(authRefreshToken);
        verify(authRepository, times(1)).findByRefreshToken(UUID.fromString(authRefreshToken.getRefreshToken()));
        verify(authRepository, times(1)).delete(any(RefreshSessions.class));
    }

    @Test
    void sessionShouldBeCreatedIfParametersValid() {
        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(authRepository.saveAndFlush(any(RefreshSessions.class))).thenReturn(expectedRefreshSession);

        authService.createRefreshSession(user, refreshToken);

        verify(userMapper, times(1)).toEntity(userDto);
        verify(authRepository, times(1)).saveAndFlush(any(RefreshSessions.class));
    }

    @Test
    void validateAndRemoveRefreshSessionShouldBeFailIfRefreshSessionNotExist() {
        when(authRepository.findByRefreshToken(UUID.fromString(authRefreshToken.getRefreshToken())))
                .thenReturn(Optional.empty());

        assertThrows(RefreshTokenNotFoundException.class, () -> authService.validateAndRemoveRefreshToken(authRefreshToken));

        verify(authRepository, times(1)).findByRefreshToken(UUID.fromString(authRefreshToken.getRefreshToken()));
        verify(authRepository, never()).delete(any(RefreshSessions.class));
        verify(userMapper, never()).toDto(user);
    }

    @Test
    void validateAndRemoveRefreshSessionShouldBeFailIfTokenIsExpired() {
        expectedRefreshSession.setExpiredAt(Instant.now().minus(Duration.ofDays(5)));
        when(authRepository.findByRefreshToken(UUID.fromString(authRefreshToken.getRefreshToken())))
                .thenReturn(Optional.of(expectedRefreshSession));

        assertThrows(TokenExpiredException.class, () -> authService.validateAndRemoveRefreshToken(authRefreshToken));

        verify(authRepository, times(1)).findByRefreshToken(UUID.fromString(authRefreshToken.getRefreshToken()));
        verify(authRepository, never()).delete(any(RefreshSessions.class));
        verify(userMapper, never()).toDto(user);
    }

    @Test
    void validateAndRemoveSessionShouldBeSuccessIfRefreshSessionExistAndNotExpired() {
        when(authRepository.findByRefreshToken(UUID.fromString(authRefreshToken.getRefreshToken())))
                .thenReturn(Optional.of(expectedRefreshSession));
        doNothing().when(authRepository).delete(any(RefreshSessions.class));
        when(userMapper.toDto(user)).thenReturn(userDto);

        authService.validateAndRemoveRefreshToken(authRefreshToken);

        verify(authRepository, times(1)).findByRefreshToken(UUID.fromString(authRefreshToken.getRefreshToken()));
        verify(authRepository, times(1)).delete(any(RefreshSessions.class));
        verify(userMapper, times(1)).toDto(user);
    }
}
