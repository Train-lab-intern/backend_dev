package com.trainlab.service.impl;

import com.trainlab.model.Session;
import com.trainlab.repository.SessionRepository;
import com.trainlab.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.mockito.Mockito.mock;

class SessionServiceImplTest {

    @InjectMocks
    private SessionServiceImpl sessionService;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    @Ignore
    void createSession() {
        Authentication authentication = mock(Authentication.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        Mockito.mock(HttpServletRequest.class);
        ServletRequestAttributes attributes = mock(ServletRequestAttributes.class);



        String sessionToken = "87704807-1251-4cdf-b2c9-294aabfd12a5";
        String sessionId = "1";
        String ipAddress = "0:0:0:0:0:0:0:1";
        Session session = Session.builder()
                .sessionToken(sessionToken)
                .sessionId(sessionId)
                .ipAddress(ipAddress)
                .created(Timestamp.valueOf(LocalDateTime.now()))
                .changed(Timestamp.valueOf(LocalDateTime.now()))
                .build();


    }
}