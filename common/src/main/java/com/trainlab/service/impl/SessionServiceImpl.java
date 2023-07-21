package com.trainlab.service.impl;

import com.trainlab.model.Session;
import com.trainlab.repository.SessionRepository;
import com.trainlab.service.SessionService;
import com.trainlab.util.RandomValuesGenerator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;

    @Override
    public void createSessionForUnauthenticatedUser(String sessionToken) {
//        Session session = new Session();
//        session.setUserId(null);
//        session.setSessionToken(sessionToken);
//        session.setCreated(Timestamp.valueOf(LocalDateTime.now()));
//        session.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String sessionId = request.getSession().getId();

        Session session = Session.builder()
                .userId(null) // Здесь можно указать нужное значение userId
                .sessionToken(sessionToken)
                .sessionId(sessionId)
                .created(Timestamp.valueOf(LocalDateTime.now()))
                .changed(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        sessionRepository.save(session);
    }
}
