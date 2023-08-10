package com.trainlab.service.impl;

import com.trainlab.model.Session;
import com.trainlab.model.User;
import com.trainlab.repository.SessionRepository;
import com.trainlab.repository.UserRepository;
import com.trainlab.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final UserRepository userRepository;

    private final SessionRepository sessionRepository;

    @Override
    public void createSession(String sessionToken, Authentication principal) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String sessionId = request.getSession().getId();
        String ipAddress = request.getRemoteAddr();

        Long userId = null;

        if (principal != null) {
            Optional<User> userOptional = userRepository.findByAuthenticationInfoEmail(principal.getName());

            if (userOptional.isPresent()) {
                userId = userOptional.get().getId();
            }
        }

        Session session = Session.builder()
                .sessionToken(sessionToken)
                .sessionId(sessionId)
                .ipAddress(ipAddress)
                .created(Timestamp.valueOf(LocalDateTime.now()))
                .changed(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        if (userId != null) {
            Optional<User> userOptional = userRepository.findById(userId);

            if (userOptional.isPresent()) {
                session.setUser(userOptional.get());
                sessionRepository.saveAndFlush(session);
            } else {
                log.error("User not found with ID: " + userId);
            }
        } else {
            sessionRepository.saveAndFlush(session);
            log.error("userId is null");
        }
    }
}