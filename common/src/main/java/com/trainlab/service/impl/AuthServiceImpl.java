package com.trainlab.service.impl;

import com.trainlab.dto.UserDto;
import com.trainlab.dto.UserPageDto;
import com.trainlab.exception.RefreshTokenNotFoundException;
import com.trainlab.exception.TokenExpiredException;
import com.trainlab.mapper.UserMapper;
import com.trainlab.model.RefreshSessions;
import com.trainlab.model.User;
import com.trainlab.model.security.AuthRefreshToken;
import com.trainlab.model.security.RefreshToken;
import com.trainlab.repository.AuthRepository;
import com.trainlab.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;

    private final AuthRepository authRepository;

    @Override
    public void createRefreshSession(User user, RefreshToken refreshToken) {
        RefreshSessions refreshSessions = RefreshSessions.builder()
                .user(user)
                .refreshToken(refreshToken.getValue())
                .issuedAt(refreshToken.getIssuedAt())
                .expiredAt(refreshToken.getExpiredAt())
                .build();

        authRepository.saveAndFlush(refreshSessions);
    }

    @Override
    public UserPageDto validateAndRemoveRefreshToken(AuthRefreshToken authRefreshToken) throws TokenExpiredException {
        RefreshSessions refreshSession = authRepository.findByRefreshToken(UUID.fromString(
                authRefreshToken.getRefreshToken())
        ).orElseThrow(() -> new RefreshTokenNotFoundException("Refresh token doesn't exist"));

        if (refreshSession.getExpiredAt().isBefore(Instant.now()))
            throw new TokenExpiredException("Token has expired");

        authRepository.delete(refreshSession);
        return userMapper.toUserPageDto(refreshSession.getUser());
    }

    @Override
    public void deleteRefreshSession(AuthRefreshToken authRefreshToken) {
        RefreshSessions refreshSession = authRepository.findByRefreshToken(UUID.fromString(
                authRefreshToken.getRefreshToken())
        ).orElseThrow(() -> new RefreshTokenNotFoundException("Refresh token doesn't exist"));

        authRepository.delete(refreshSession);
    }
}
