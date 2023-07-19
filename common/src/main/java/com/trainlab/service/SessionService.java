package com.trainlab.service;

public interface SessionService {
//    void save(Session session);
    void createSessionForUnauthenticatedUser(String sessionToken);

}
