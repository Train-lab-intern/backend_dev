package com.trainlab.service;

import com.trainlab.dto.UserCreateDto;
import com.trainlab.dto.UserDto;
import com.trainlab.dto.UserUpdateDto;
import com.trainlab.model.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {
    User create(UserCreateDto userCreateDto);

    void activateUser(String userEmail);

    UserDto findAuthorizedUser(Long id, UserDetails userDetails);

    List<UserDto> findAll();

    UserDto update(UserUpdateDto userUpdateDto, Long id, UserDetails userDetails);

    User findByEmail(String email);

    void changePassword(Long id, UserUpdateDto userUpdateDto);
}
