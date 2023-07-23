package com.trainlab.service.impl;

import com.trainlab.model.Session;
import com.trainlab.repository.SessionRepository;
import com.trainlab.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;

    @Override
    public void createSessionForUser(String sessionToken, Long userId) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String sessionId = request.getSession().getId();

        Session session = Session.builder()
                .userId(userId) // Здесь используем переданный userId
                .sessionToken(sessionToken)
                .sessionId(sessionId)
                .created(Timestamp.valueOf(LocalDateTime.now()))
                .changed(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        sessionRepository.save(session);
    }

    @Override
    public void updateUserIdInSessions(Long userId) {
        List<Session> sessions = sessionRepository.findSessionByUserId(null);
        for (Session session : sessions) {
            session.setUserId(userId);
            sessionRepository.save(session);
        }
    }
}
