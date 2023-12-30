package com.trainlab.service;

import com.trainlab.dto.AuthRequestDto;
import com.trainlab.dto.UserCreateDto;
import com.trainlab.dto.UserDto;
import com.trainlab.dto.UserUpdateDto;
import com.trainlab.enums.Speciality;
import com.trainlab.enums.UserLevel;
import com.trainlab.exception.UsernameGenerationException;


import java.util.List;

public interface UserService {
    UserDto create(UserCreateDto userCreateDto) throws UsernameGenerationException;

/*    void activateUser(String userEmail);*/

    UserDto findAuthorizedUser(Long id);

    List<UserDto> findAll();

    UserDto update(UserUpdateDto userUpdateDto, Long id);

    UserDto findUserByAuthenticationInfo(AuthRequestDto authRequestDto);

    void changePassword(Long id, UserUpdateDto userUpdateDto);

}
