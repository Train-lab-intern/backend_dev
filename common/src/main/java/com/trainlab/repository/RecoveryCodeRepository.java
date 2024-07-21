package com.trainlab.repository;

import com.trainlab.model.User;
import com.trainlab.model.recovery.RecoveryCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecoveryCodeRepository extends JpaRepository<RecoveryCode, Long> {

    RecoveryCode findRecoveryCodeByUser(User user);
}
