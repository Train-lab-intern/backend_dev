package com.trainlab.service;

import com.trainlab.dto.*;
import com.trainlab.exception.UsernameGenerationException;


import java.util.List;

public interface UserService {
    UserPageDto create(UserCreateDto userCreateDto);

/*    void activateUser(String userEmail);*/

    UserDto findAuthorizedUser(Long id);

    List<UserDto> findAll();

    UserDto update(UserUpdateDto userUpdateDto, Long id);

    UserDto findUserByAuthenticationInfo(AuthRequestDto authRequestDto);

    void changePassword(Long id, UserUpdateDto userUpdateDto);
}
