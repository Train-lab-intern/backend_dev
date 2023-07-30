package com.trainlab.service;

import com.trainlab.dto.request.UserRequest;
import com.trainlab.model.User;

import java.util.List;

public interface UserService {
    User create(UserRequest userRequest);

    void activateUser(String userEmail);

    User findById(Long id);

    List<User> findAll();

    User update(UserRequest userRequest, Long id);
}
