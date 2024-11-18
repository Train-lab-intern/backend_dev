package java.com.trainlab.service.recovery;

import com.trainlab.dto.auth.AuthRequestDto;
import com.trainlab.dto.recovery.EmailRequestDto;
import com.trainlab.dto.recovery.RecoveryCodeDto;
import com.trainlab.exception.recovery.IllegalRecoveryCodeException;
import com.trainlab.exception.recovery.RateLimitExceededException;
import com.trainlab.exception.recovery.RecoveryCodeExpiredException;
import com.trainlab.mapper.UserMapper;
import com.trainlab.model.AuthenticationInfo;
import com.trainlab.model.User;
import com.trainlab.model.recovery.RecoveryCode;
import com.trainlab.repository.UserRepository;
import com.trainlab.repository.recovery.RecoveryCodeRepository;
import com.trainlab.service.email.EmailService;
import com.trainlab.service.recovery.PasswordRecoveryServiceImpl;
import com.trainlab.util.password.CustomPasswordEncoder;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PasswordRecoveryServiceImplTest {

    private static final Integer CODE_TIME_TO_LIVE = 5;
    @Mock
    private RecoveryCodeRepository recoveryCodeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private CustomPasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private PasswordRecoveryServiceImpl passwordRecoveryService;

    private EmailRequestDto emailRequestDto;

    private User user;

    private RecoveryCode recoveryCode;

    private AuthRequestDto authRequestDto;

    @BeforeEach
    void init() {
        emailRequestDto = EmailRequestDto.builder()
                .email("vlad12345@gmail.com").build();
        user = User.builder()
                .id(1L)
                .authenticationInfo(AuthenticationInfo.builder()
                        .email(emailRequestDto.getEmail())
                        .userPassword("12345678").build())
                .build();
        recoveryCode = RecoveryCode.builder()
                .user(user)
                .code("4356761276")
                .createdAt(OffsetDateTime.now(ZoneId.of("Europe/Minsk")))
                .expiredAt(OffsetDateTime.now(ZoneId.of("Europe/Minsk")).plusMinutes(CODE_TIME_TO_LIVE))
                .build();
        authRequestDto = AuthRequestDto.builder()
                .email(emailRequestDto.getEmail())
                .password("vlad12345132Q")
                .build();
    }

    @Nested
    @DisplayName("reset password functional")
    @Tag("reset-password")
    class ResetPassword {

        @Test
        void resetPasswordFailedIfEntityNotFound() {
            when(userRepository.findByAuthenticationInfoEmailAndIsDeletedFalse(emailRequestDto.getEmail()))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> passwordRecoveryService.resetPassword(emailRequestDto))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("User couldn't be found");
            verify(userRepository, times(1)).findByAuthenticationInfoEmailAndIsDeletedFalse(emailRequestDto.getEmail());
            verify(recoveryCodeRepository, never()).findRecoveryCodeByUser(any(User.class));
            verify(recoveryCodeRepository, never()).delete(any(RecoveryCode.class));
        }

        @Test
        void resetPasswordThrowExceptionIfUserSendsTooManyRequests() {
            when(userRepository.findByAuthenticationInfoEmailAndIsDeletedFalse(emailRequestDto.getEmail())).thenReturn(Optional.of(user));
            when(recoveryCodeRepository.findRecoveryCodeByUser(user)).thenReturn(Optional.of(recoveryCode));

            assertThatThrownBy(() -> passwordRecoveryService.resetPassword(emailRequestDto))
                    .isInstanceOf(RateLimitExceededException.class);

            verify(recoveryCodeRepository, times(1)).findRecoveryCodeByUser(user);
            verify(userRepository, times(1)).findByAuthenticationInfoEmailAndIsDeletedFalse(emailRequestDto.getEmail());
        }

        @Test
        void resetPasswordIsSuccessfulIfUserExistsAndRecoveryCodeExpired() {
            recoveryCode.setCreatedAt(OffsetDateTime.now(ZoneId.of("Europe/Minsk")).minusMinutes(CODE_TIME_TO_LIVE));
            recoveryCode.setExpiredAt(OffsetDateTime.now(ZoneId.of("Europe/Minsk")));

            when(userRepository.findByAuthenticationInfoEmailAndIsDeletedFalse(emailRequestDto.getEmail())).thenReturn(Optional.of(user));
            when(recoveryCodeRepository.findRecoveryCodeByUser(user)).thenReturn(Optional.of(recoveryCode));
            when(recoveryCodeRepository.saveAndFlush(any(RecoveryCode.class))).thenReturn(recoveryCode);
            doNothing().when(recoveryCodeRepository).delete(recoveryCode);
            doNothing().when(emailService).sendNewPassword(any(), any());

            passwordRecoveryService.resetPassword(emailRequestDto);

            verify(userRepository, times(1)).findByAuthenticationInfoEmailAndIsDeletedFalse(emailRequestDto.getEmail());
            verify(recoveryCodeRepository, times(1)).findRecoveryCodeByUser(user);
            verify(recoveryCodeRepository, times(1)).delete(recoveryCode);
            verify(recoveryCodeRepository, times(1)).saveAndFlush(any(RecoveryCode.class));
            verify(emailService, times(1)).sendNewPassword(anyString(), anyString());
        }

        @Test
        void resetPasswordSuccessfulIfUserExistsAndRecoveryCodeDoesNotExist() {
            when(userRepository.findByAuthenticationInfoEmailAndIsDeletedFalse(emailRequestDto.getEmail())).thenReturn(Optional.of(user));
            when(recoveryCodeRepository.findRecoveryCodeByUser(user)).thenReturn(Optional.empty());
            when(recoveryCodeRepository.saveAndFlush(any(RecoveryCode.class))).thenReturn(recoveryCode);
            doNothing().when(emailService).sendNewPassword(any(), any());

            passwordRecoveryService.resetPassword(emailRequestDto);

            verify(userRepository, times(1)).findByAuthenticationInfoEmailAndIsDeletedFalse(emailRequestDto.getEmail());
            verify(recoveryCodeRepository, times(1)).findRecoveryCodeByUser(user);
            verify(recoveryCodeRepository, times(1)).saveAndFlush(any(RecoveryCode.class));
            verify(emailService, times(1)).sendNewPassword(anyString(), anyString());
        }
    }

    @Nested
    @DisplayName("Verify code functional")
    @Tag("verify-code")
    class CodeVerification {

        private RecoveryCodeDto recoveryCodeDto;

        @BeforeEach
        void init() {
            recoveryCodeDto = RecoveryCodeDto.builder()
                    .email(emailRequestDto.getEmail())
                    .code("4356761276").build();
        }

        @Test
        void verifyCodeThrowExceptionIfUserDoesNotExist() {
            when(userRepository.findByAuthenticationInfoEmailAndIsDeletedFalse(emailRequestDto.getEmail())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> passwordRecoveryService.verifyCode(recoveryCodeDto))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("User couldn't be found");

            verify(userRepository, times(1)).findByAuthenticationInfoEmailAndIsDeletedFalse(emailRequestDto.getEmail());
            verify(recoveryCodeRepository, never()).findRecoveryCodeByUser(user);
            verify(recoveryCodeRepository, never()).delete(any(RecoveryCode.class));
        }

        @Test
        void verifyCodeThrowExceptionIfRecoveryCodeDoesNotExist() {
            when(userRepository.findByAuthenticationInfoEmailAndIsDeletedFalse(emailRequestDto.getEmail())).thenReturn(Optional.of(user));
            when(recoveryCodeRepository.findRecoveryCodeByUser(user)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> passwordRecoveryService.verifyCode(recoveryCodeDto))
                    .isInstanceOf(IllegalRecoveryCodeException.class)
                    .hasMessage("Recovery code couldn't be found");

            verify(userRepository, times(1)).findByAuthenticationInfoEmailAndIsDeletedFalse(emailRequestDto.getEmail());
            verify(recoveryCodeRepository, times(1)).findRecoveryCodeByUser(user);
            verify(recoveryCodeRepository, never()).delete(any(RecoveryCode.class));
        }

        @Test
        void verifyCodeThrowExceptionIfRecoveryCodeDoesNotMatch() {
            RecoveryCodeDto wrongCode = RecoveryCodeDto.builder()
                    .email(emailRequestDto.getEmail())
                    .code("3245178542").build();

            when(userRepository.findByAuthenticationInfoEmailAndIsDeletedFalse(emailRequestDto.getEmail())).thenReturn(Optional.of(user));
            when(recoveryCodeRepository.findRecoveryCodeByUser(user)).thenReturn(Optional.of(recoveryCode));

            assertThatThrownBy(() -> passwordRecoveryService.verifyCode(wrongCode))
                    .isInstanceOf(IllegalRecoveryCodeException.class)
                    .hasMessage("Illegal recovery code");

            verify(userRepository, times(1)).findByAuthenticationInfoEmailAndIsDeletedFalse(emailRequestDto.getEmail());
            verify(recoveryCodeRepository, times(1)).findRecoveryCodeByUser(user);
            verify(recoveryCodeRepository, never()).delete(any(RecoveryCode.class));
        }

        @Test
        void verifyCodeThrowExceptionIfCodeIsExpired() {
            recoveryCode.setExpiredAt(OffsetDateTime.now(ZoneId.of("Europe/Minsk")).minusMinutes(1));
            recoveryCode.setCreatedAt(recoveryCode.getExpiredAt().minusMinutes(CODE_TIME_TO_LIVE));

            when(userRepository.findByAuthenticationInfoEmailAndIsDeletedFalse(emailRequestDto.getEmail())).thenReturn(Optional.of(user));
            when(recoveryCodeRepository.findRecoveryCodeByUser(user)).thenReturn(Optional.of(recoveryCode));
            doNothing().when(recoveryCodeRepository).delete(any(RecoveryCode.class));

            assertThatThrownBy(() -> passwordRecoveryService.verifyCode(recoveryCodeDto))
                    .isInstanceOf(RecoveryCodeExpiredException.class)
                    .hasMessage("Recovery code has expired");

            verify(userRepository, times(1)).findByAuthenticationInfoEmailAndIsDeletedFalse(emailRequestDto.getEmail());
            verify(recoveryCodeRepository, times(1)).findRecoveryCodeByUser(user);
            verify(recoveryCodeRepository, times(1)).delete(any(RecoveryCode.class));
        }

        @Test
        void verifyCodeThrowExceptionIfUserAndRecoveryCodeExistAndAreNotExpired() {
            when(userRepository.findByAuthenticationInfoEmailAndIsDeletedFalse(emailRequestDto.getEmail())).thenReturn(Optional.of(user));
            when(recoveryCodeRepository.findRecoveryCodeByUser(user)).thenReturn(Optional.of(recoveryCode));
            doNothing().when(recoveryCodeRepository).delete(any(RecoveryCode.class));

            passwordRecoveryService.verifyCode(recoveryCodeDto);

            verify(userRepository, times(1)).findByAuthenticationInfoEmailAndIsDeletedFalse(emailRequestDto.getEmail());
            verify(recoveryCodeRepository, times(1)).findRecoveryCodeByUser(user);
            verify(recoveryCodeRepository, times(1)).delete(any(RecoveryCode.class));
        }
    }

    @Test
    void changePasswordThrowExceptionIfUserDoesNotExist() {
        when(userRepository.findByAuthenticationInfoEmailAndIsDeletedFalse(emailRequestDto.getEmail())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> passwordRecoveryService.changePassword(authRequestDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("User couldn't be found");

        verify(userRepository, times(1)).findByAuthenticationInfoEmailAndIsDeletedFalse(emailRequestDto.getEmail());
        verify(userRepository, never()).saveAndFlush(user);
    }

    @Test
    void changePasswordSuccessfulIfUserExists() {

        when(userRepository.findByAuthenticationInfoEmailAndIsDeletedFalse(emailRequestDto.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.saveAndFlush(user)).thenReturn(user);

        assertThat(passwordRecoveryService.changePassword(authRequestDto)).isEqualTo(userMapper.toUserPageDto(user));

        verify(userRepository, times(1)).findByAuthenticationInfoEmailAndIsDeletedFalse(emailRequestDto.getEmail());
        verify(userRepository, times(1)).saveAndFlush(user);
    }
}
