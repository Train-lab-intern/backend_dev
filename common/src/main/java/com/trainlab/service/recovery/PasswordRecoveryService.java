package com.trainlab.service.recovery;

import com.trainlab.dto.auth.AuthRequestDto;
import com.trainlab.dto.UserPageDto;
import com.trainlab.dto.recovery.EmailRequestDto;
import com.trainlab.dto.recovery.RecoveryCodeDto;

public interface PasswordRecoveryService {

    void verifyCode(RecoveryCodeDto recoveryCodeDto);

    void resetPassword(EmailRequestDto emailRequestDto);

    UserPageDto changePassword(AuthRequestDto auth);
}
