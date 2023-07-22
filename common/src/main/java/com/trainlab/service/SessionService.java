package com.trainlab.service;

public interface SessionService {
//    void save(Session session);
    void createSessionForUser(String sessionToken, Long userId);

}
