package com.trainlab.controller.recovery;

import com.trainlab.dto.UserPageDto;
import com.trainlab.dto.auth.AuthRequestDto;
import com.trainlab.dto.recovery.EmailRequestDto;
import com.trainlab.dto.recovery.RecoveryCodeDto;
import com.trainlab.dto.auth.AuthResponseDto;
import com.trainlab.dto.recovery.PasswordRecoveryMessage;
import com.trainlab.service.recovery.PasswordRecoveryService;
import com.trainlab.service.token.TokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.trainlab.util.ValidationUtil.isRequestValid;

@RestController
@RequiredArgsConstructor
@Tag(name = "Password recovery controller", description = "Password recovery")
@RequestMapping("/api/v1/reset-password")
public class PasswordRecoveryControllerImpl implements PasswordRecoveryController {

    private final PasswordRecoveryService passwordRecoveryService;
    private final TokenService tokenService;

    @Override
    @PostMapping
    public ResponseEntity<PasswordRecoveryMessage> resetPassword(@Valid @RequestBody EmailRequestDto emailRequestDto,
                                                                 BindingResult bindingResult) {
        isRequestValid(bindingResult);
        passwordRecoveryService.resetPassword(emailRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(PasswordRecoveryMessage.builder()
                .message("The code has been successfully sent. Check your email")
                .build());
    }

    @Override
    @PostMapping("/verify")
    public ResponseEntity<PasswordRecoveryMessage> verifyCode(@Valid @RequestBody RecoveryCodeDto recoveryCodeDto,
                                             BindingResult bindingResult) {
        isRequestValid(bindingResult);
        passwordRecoveryService.verifyCode(recoveryCodeDto);
        return ResponseEntity.status(HttpStatus.OK).body(PasswordRecoveryMessage.builder()
                .message("Verified successfully")
                .build());
    }

    @Override
    @PatchMapping("/create-new-password")
    public ResponseEntity<AuthResponseDto> createNewPassword(@Valid @RequestBody AuthRequestDto authRequestDto,
                                                             BindingResult bindingResult) {
        isRequestValid(bindingResult);
        UserPageDto userPageDto = passwordRecoveryService.changePassword(authRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(tokenService.generateTokensAndCreateSession(userPageDto));
    }
}
