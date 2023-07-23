package com.trainlab.service;

import com.trainlab.dto.request.UserRequest;
import com.trainlab.model.User;

public interface UserService {
    User create(UserRequest userRequest);

    void activateUser(String userEmail);
}
