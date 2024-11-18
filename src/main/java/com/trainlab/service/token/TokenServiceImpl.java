package com.trainlab.service.token;

import com.trainlab.dto.UserPageDto;
import com.trainlab.dto.auth.AuthResponseDto;
import com.trainlab.model.security.AccessToken;
import com.trainlab.model.security.RefreshToken;
import com.trainlab.security.TokenProvider;
import com.trainlab.security.principal.UserPrincipal;
import com.trainlab.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final TokenProvider tokenProvider;
    private final AuthService authService;

    public AuthResponseDto generateTokensAndCreateSession(UserPageDto userPageDto) {
        AccessToken token = tokenProvider.generate(new UserPrincipal(userPageDto.getId(), userPageDto.getRoles()));
        RefreshToken refreshToken = tokenProvider.generateRefreshToken();
        authService.createRefreshSession(userPageDto, refreshToken);

        return AuthResponseDto.builder()
                        .token(token)
                        .refreshToken(refreshToken)
                        .userPageDto(userPageDto)
                        .build();
    }
}
