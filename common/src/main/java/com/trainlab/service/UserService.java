package com.trainlab.service;

import com.trainlab.dto.request.UserCreateRequest;
import com.trainlab.dto.request.UserUpdateRequest;
import com.trainlab.model.User;

import java.security.Principal;
import java.util.List;

public interface UserService {
    User create(UserCreateRequest userCreateRequest);

    void activateUser(String userEmail);

    User findById(Long id, Principal principal);

    List<User> findAll();

    User update(UserUpdateRequest userUpdateRequest, Long id, Principal principal);
    void changePassword(UserUpdateRequest userUpdateRequest, Long id);
}
