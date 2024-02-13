package com.trainlab.service;

import com.trainlab.dto.*;
import com.trainlab.exception.UsernameGenerationException;
import com.trainlab.model.User;


import java.util.List;

public interface UserService {
    User create(UserCreateDto userCreateDto);

/*    void activateUser(String userEmail);*/

    User findAuthorizedUser(Long id);

    List<UserDto> findAll();

    UserPageDto update(UserPageUpdateDto userUpdateDto, Long id);

    User findUserByAuthenticationInfo(AuthRequestDto authRequestDto);

    void changePassword(Long id, UserUpdateDto userUpdateDto);
}
