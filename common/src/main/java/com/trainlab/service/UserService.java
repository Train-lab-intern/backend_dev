package com.trainlab.service;

import com.trainlab.dto.request.UserRequest;
import com.trainlab.model.TrainlabUser;

public interface UserService {
    TrainlabUser create(UserRequest userRequest);

    void activateUser(String userEmail);
}
