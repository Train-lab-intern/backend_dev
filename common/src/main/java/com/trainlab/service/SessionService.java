package com.trainlab.service;

public interface SessionService {
    void createSessionForUser(String sessionToken, Long userId);

    void updateUserIdInSessions(Long userId);
}
