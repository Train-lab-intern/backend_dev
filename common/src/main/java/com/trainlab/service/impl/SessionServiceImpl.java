package com.trainlab.service.impl;

import com.trainlab.model.Session;
import com.trainlab.repository.SessionRepository;
import com.trainlab.repository.UserRepository;
import com.trainlab.service.SessionService;
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

    private final UserRepository userRepository;

    @Override
    public void createSessionForUser(String sessionToken, Long userId) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String sessionId = request.getSession().getId();

//        User user = userRepository.findById(userId).orElse(null);
//
//        if (user == null) {
//            return;
//        }

        Session session = Session.builder()
                .userId(userId)
                .sessionToken(sessionToken)
                .sessionId(sessionId)
                .created(Timestamp.valueOf(LocalDateTime.now()))
                .changed(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        sessionRepository.save(session);
    }
}
