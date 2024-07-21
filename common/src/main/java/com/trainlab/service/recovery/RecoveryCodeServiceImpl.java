package com.trainlab.service.recovery;

import com.trainlab.dto.UserPageDto;
import com.trainlab.dto.recovery.EmailRequestDto;
import com.trainlab.dto.recovery.RecoveryCodeDto;
import com.trainlab.exception.recovery.IllegalRecoveryCodeException;
import com.trainlab.exception.recovery.RecoveryCodeExpiredException;
import com.trainlab.mapper.UserMapper;
import com.trainlab.model.User;
import com.trainlab.model.recovery.RecoveryCode;
import com.trainlab.repository.RecoveryCodeRepository;
import com.trainlab.repository.UserRepository;
import com.trainlab.service.EmailService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecoveryCodeServiceImpl implements RecoveryCodeService {

    private static final Integer CODE_TIME_TO_LIVE = 5;
    private final RecoveryCodeRepository recoveryCodeRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    private final UserMapper userMapper;

    @Override
    public void resetPassword(EmailRequestDto emailRequestDto) {
        User user = userRepository.findByAuthenticationInfoEmailAndIsDeletedFalse(emailRequestDto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User could not be found"));

        String toAddress = user.getAuthenticationInfo().getEmail();
        String code = RandomStringUtils.randomNumeric(10);

        RecoveryCode recoveryCode = RecoveryCode.builder()
                .code(code)
                .expiredAt(Instant.now().plusSeconds(TimeUnit.MINUTES.toSeconds(CODE_TIME_TO_LIVE)))
                .build();
        recoveryCode.setUser(user);

        recoveryCodeRepository.saveAndFlush(recoveryCode);
        emailService.sendNewPassword(toAddress, code);
    }

    @Override
    public UserPageDto verifyCode(RecoveryCodeDto recoveryCodeDto) {
        User user = userRepository.findByAuthenticationInfoEmail(recoveryCodeDto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User could not be found"));

        RecoveryCode recoveryCode = recoveryCodeRepository.findRecoveryCodeByUser(user);

        // Подумать над проверкой на null
        if (recoveryCode == null || !recoveryCode.getCode().equals(recoveryCodeDto.getCode()))
            throw new IllegalRecoveryCodeException("Illegal recovery code");

        if (recoveryCode.getExpiredAt().isBefore(Instant.now())) {
            recoveryCodeRepository.delete(recoveryCode);
            throw new RecoveryCodeExpiredException("Recovery code has expired");
        }

        recoveryCodeRepository.delete(recoveryCode);

        return userMapper.toUserPageDto(user);
    }
}
