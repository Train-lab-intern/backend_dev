package com.trainlab.service;

import com.trainlab.dto.UserDto;
import com.trainlab.dto.UserPageDto;
import com.trainlab.exception.TokenExpiredException;
import com.trainlab.model.security.AuthRefreshToken;
import com.trainlab.model.security.RefreshToken;

public interface AuthService {

    void createRefreshSession(UserPageDto userDto, RefreshToken refreshToken);

    UserPageDto validateAndRemoveRefreshToken(AuthRefreshToken authRefreshToken) throws TokenExpiredException;

    void deleteRefreshSession(AuthRefreshToken authRefreshToken);
}
