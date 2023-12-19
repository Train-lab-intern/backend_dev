package com.trainlab.service;

import com.trainlab.dto.UserDto;
import com.trainlab.exception.TokenExpiredException;
import com.trainlab.model.security.AuthRefreshToken;
import com.trainlab.model.security.RefreshToken;

public interface AuthService {

    void createRefreshSession(UserDto userDto, RefreshToken refreshToken);

    UserDto validateAndRemoveRefreshToken(AuthRefreshToken authRefreshToken) throws TokenExpiredException;

    void deleteRefreshSession(AuthRefreshToken authRefreshToken);
}
