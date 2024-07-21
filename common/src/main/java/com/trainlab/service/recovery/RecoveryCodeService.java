package com.trainlab.service.recovery;

import com.trainlab.dto.UserPageDto;
import com.trainlab.dto.recovery.EmailRequestDto;
import com.trainlab.dto.recovery.RecoveryCodeDto;

public interface RecoveryCodeService {

    UserPageDto verifyCode(RecoveryCodeDto recoveryCodeDto);

    void resetPassword(EmailRequestDto emailRequestDto);
}
