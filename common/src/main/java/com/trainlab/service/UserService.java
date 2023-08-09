package com.trainlab.service;

import com.trainlab.dto.UserCreateRequestDto;
import com.trainlab.dto.UserUpdateRequestDto;
import com.trainlab.model.User;

import java.security.Principal;
import java.util.List;

public interface UserService {
    User create(UserCreateRequestDto userCreateRequestDto);

    void activateUser(String userEmail);

    User findById(Long id, Principal principal);

    List<User> findAll();

    User update(UserUpdateRequestDto userUpdateRequestDto, Long id, Principal principal);

    User findByEmail(String email);

    void changePassword(UserUpdateRequestDto userUpdateRequestDto, Long id);
}
