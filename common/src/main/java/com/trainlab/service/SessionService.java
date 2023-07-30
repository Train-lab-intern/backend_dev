package com.trainlab.service;

import java.security.Principal;

public interface SessionService {
    void createSession(String sessionToken, Principal principal);
}
