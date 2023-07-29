package com.trainlab.service;

public interface SessionService {
    void createSession(String sessionToken, Long userId);
}
