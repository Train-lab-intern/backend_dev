package com.trainlab.service.token;

import com.trainlab.dto.UserPageDto;
import com.trainlab.dto.auth.AuthResponseDto;

public interface TokenService {

    AuthResponseDto generateTokensAndCreateSession(UserPageDto userPageDto);

}
