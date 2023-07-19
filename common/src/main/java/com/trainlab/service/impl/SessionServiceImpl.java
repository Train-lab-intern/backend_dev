package com.trainlab.service.impl;

import com.trainlab.model.Session;
import com.trainlab.repository.SessionRepository;
import com.trainlab.service.SessionService;
import com.trainlab.util.RandomValuesGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;

    @Override
    public void createSessionForUnauthenticatedUser(String sessionToken) {
        Session session = new Session();
        session.setSessionToken(sessionToken);
        session.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        session.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        sessionRepository.save(session);
    }
}
