package com.trainlab.service;

import org.springframework.security.core.Authentication;

public interface SessionService {
    void createSession(String sessionToken, Authentication principal);
}
