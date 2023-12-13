package com.trainlab.service;

import com.trainlab.dto.UserDto;
import com.trainlab.exception.InvalidRefreshSession;
import com.trainlab.exception.TokenExpiredException;
import com.trainlab.model.ClientData;
import com.trainlab.model.security.AuthRefreshToken;
import com.trainlab.model.security.RefreshToken;

public interface AuthService {

    void createRefreshSession(ClientData clientData, UserDto userDto, RefreshToken refreshToken);

    UserDto validateAndRemoveRefreshToken(AuthRefreshToken authRefreshToken) throws TokenExpiredException, InvalidRefreshSession;

    void deleteRefreshSession(AuthRefreshToken authRefreshToken) throws InvalidRefreshSession;
}
