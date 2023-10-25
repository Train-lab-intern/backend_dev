package com.trainlab.repository;

import com.trainlab.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findSessionByUserId(Long userId);
}