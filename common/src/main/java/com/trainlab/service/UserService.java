package com.trainlab.service;

import com.trainlab.dto.UserCreateDto;
import com.trainlab.dto.UserUpdateDto;
import com.trainlab.model.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {
    User create(UserCreateDto userCreateDto);

    void activateUser(String userEmail);

    User findById(Long id, UserDetails userDetails);

    List<User> findAll();

    User update(UserUpdateDto userUpdateDto, Long id, UserDetails userDetails);

    User findByEmail(String email);

    void changePassword(UserUpdateDto userUpdateDto, Long id);
}
