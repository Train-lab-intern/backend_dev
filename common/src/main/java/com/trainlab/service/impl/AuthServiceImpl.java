package com.trainlab.service.impl;

import com.trainlab.dto.UserDto;
import com.trainlab.exception.InvalidRefreshSession;
import com.trainlab.exception.TokenExpiredException;
import com.trainlab.mapper.UserMapper;
import com.trainlab.model.ClientData;
import com.trainlab.model.RefreshSessions;
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
    public void createRefreshSession(ClientData clientData, UserDto userDto, RefreshToken refreshToken) {
        RefreshSessions refreshSessions = RefreshSessions.builder()
                .user(userMapper.toEntity(userDto))
                .refreshToken(refreshToken.getValue())
                .fingerprint(clientData.getFingerprint())
                .issuedAt(refreshToken.getIssuedAt())
                .expiredAt(refreshToken.getExpiredAt())
                .build();

        authRepository.saveAndFlush(refreshSessions);
    }

    @Override
    public UserDto validateAndRemoveRefreshToken(AuthRefreshToken authRefreshToken) throws TokenExpiredException, InvalidRefreshSession {
        RefreshSessions refreshSession = authRepository.findByRefreshToken(UUID.fromString(
                authRefreshToken.getRefreshToken())
        ).orElseThrow(() -> new IllegalArgumentException("Refresh token doesn't exist."));

        boolean isBefore = refreshSession.getExpiredAt().isBefore(Instant.now());
        boolean isFingerprintValid = refreshSession.getFingerprint().equals(authRefreshToken.getFingerprint());

        if (isBefore)
            throw new TokenExpiredException("Token has expired.");
        else if (!isFingerprintValid)
            throw new InvalidRefreshSession("Invalid refresh session.");

        authRepository.delete(refreshSession);
        return userMapper.toDto(refreshSession.getUser());
    }

    @Override
    public void deleteRefreshSession(AuthRefreshToken authRefreshToken) throws InvalidRefreshSession {
        RefreshSessions refreshSession = authRepository.findByRefreshToken(UUID.fromString(
                authRefreshToken.getRefreshToken())
        ).orElseThrow(() -> new IllegalArgumentException("Refresh token doesn't exist."));

        if (!refreshSession.getFingerprint().equals(authRefreshToken.getFingerprint()))
            throw new InvalidRefreshSession("Invalid refresh session.");

        authRepository.delete(refreshSession);
    }
}
