package com.trainlab.repository.recovery;

import com.trainlab.model.User;
import com.trainlab.model.recovery.RecoveryCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecoveryCodeRepository extends JpaRepository<RecoveryCode, Long> {

    Optional<RecoveryCode> findRecoveryCodeByUser(User user);
}
