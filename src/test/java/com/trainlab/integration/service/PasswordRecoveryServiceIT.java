package java.com.trainlab.integration.service;

import com.trainlab.dto.recovery.EmailRequestDto;
import com.trainlab.service.recovery.PasswordRecoveryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
public class PasswordRecoveryServiceIT {

    private final PasswordRecoveryService passwordRecoveryService;

    @Test
    void resetPasswordShouldThrowExceptionIfUserDoesNotExist() {
        assertThatThrownBy(() -> passwordRecoveryService.resetPassword(EmailRequestDto.builder()
                .email("vlad123@gmail.com").build()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("User couldn't be found");
    }
}