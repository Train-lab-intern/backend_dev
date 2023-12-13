package com.trainlab.repository;

import com.trainlab.model.RefreshSessions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthRepository extends JpaRepository<RefreshSessions, Long> {

    Optional<RefreshSessions> findByRefreshToken(UUID refreshToken);

}
