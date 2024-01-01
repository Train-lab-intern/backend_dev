package com.trainlab.service.impl;

import com.trainlab.dto.*;
import com.trainlab.exception.IllegalRequestException;
import com.trainlab.exception.ObjectNotFoundException;
import com.trainlab.exception.UsernameGenerationException;
import com.trainlab.mapper.RoleMapper;
import com.trainlab.mapper.UserMapper;
import com.trainlab.model.Role;
import com.trainlab.model.User;
import com.trainlab.repository.RoleRepository;
import com.trainlab.repository.UserRepository;
import com.trainlab.service.EmailService;
import com.trainlab.service.UserService;
import com.trainlab.util.PasswordEncode;
import com.trainlab.util.RandomValuesGenerator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final RandomValuesGenerator generator;

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;

    private final PasswordEncode passwordEncoder;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final EmailService emailService;

    @Override
    public UserDto create(UserCreateDto userCreateDto) throws UsernameGenerationException {
        User user = userMapper.toEntity(userCreateDto);
        String email = user.getAuthenticationInfo().getEmail().toLowerCase();
        user.getAuthenticationInfo().setEmail(email);

        checkUserEmailAndPasswordExist(user);
        setEncodedPassword(user);
        List<Role> userRoles = setDefaultRole(user);
        userRepository.saveAndFlush(user);

        user.setUsername(generateUsername(user.getId()));
        userRepository.save(user);

        List<RoleDto> roleDto = roleMapper.toListDto(userRoles);
        /*        buildEmailMessage(user);*/
        UserDto userDto = userMapper.toDto(user);
        userDto.setRoles(roleDto);
        return userDto;
    }

    @Override
    public UserDto findUserByAuthenticationInfo(AuthRequestDto authRequestDto) {
        User user = userRepository.findByAuthenticationInfoEmailAndIsDeletedFalse(authRequestDto
                .getUserEmail()
                .toLowerCase())
                .orElseThrow(() -> new ObjectNotFoundException("User not found with email: " + authRequestDto.getUserEmail()));

        boolean isPasswordMatches = passwordEncoder.matches(
                authRequestDto.getUserPassword(),
                user.getAuthenticationInfo().getUserPassword()
        );

        if (!isPasswordMatches)
            throw new BadCredentialsException("Invalid login or password");

        return userMapper.toDto(user);
    }


/*    @Override
    public void activateUser(String userEmail) {
        UserDto user = findByEmail(userEmail);

        if (user.getIsActive()) {
            throw new IllegalStateException("User with email " + userEmail + " is yet activate.");
        }

        user.setIsActive(true);
        user.setChanged(Timestamp.valueOf(LocalDateTime.now().withNano(0)));
        userRepository.saveAndFlush(userMapper.toEntity(user));
        log.info("User with email " + userEmail + " activate successfully!");
    }*/

    @Override
    public UserDto findAuthorizedUser(Long id) {
        User user = findByIdAndIsDeletedFalse(id);
            return userMapper.toDto(user);
    }

    @Override
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAllByIsDeletedFalseOrderById();
        return users.stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public UserDto update(UserUpdateDto userUpdateDto, Long id) {
        UserDto userDto = findAuthorizedUser(id);
        User user = userMapper.toEntity(userDto);

        User updated = userMapper.partialUpdateToEntity(userUpdateDto, user);
        setEncodedPassword(updated);

        checkUserEmailAndPasswordExist(updated);

        return userMapper.toDto(userRepository.saveAndFlush(updated));
    }

    private void checkUserEmailAndPasswordExist(User updated) {
//        Optional<User> checkUsername = userRepository.findByUsername(updated.getUsername());
        Optional<User> checkUserEmail = userRepository.findByAuthenticationInfoEmail(updated.getAuthenticationInfo().getEmail());

//        if (checkUsername.isPresent()) {
//            throw new IllegalRequestException("User with this username is already exists.");
//        }
         if (checkUserEmail.isPresent()) {
            throw new IllegalRequestException("User with this email is already exists");
        }
    }

    @Override
    public void changePassword(Long id, UserUpdateDto userUpdateDto) {
        User user = findByIdAndIsDeletedFalse(id);
        String toAddress = userUpdateDto.getEmail();

        String newPassword = generator.generateRandomPassword(8);
        String encodedPassword = passwordEncoder.encodePassword(newPassword);
        user.getAuthenticationInfo().setUserPassword(encodedPassword);

        userRepository.saveAndFlush(user);
        emailService.sendNewPassword(toAddress, newPassword);
    }

    private void setEncodedPassword(User user) {
        String encodedPassword = passwordEncoder.encodePassword(user.getAuthenticationInfo().getUserPassword());
        user.getAuthenticationInfo().setUserPassword(encodedPassword);
    }

    private List<Role> setDefaultRole(User user) {
        Role userRole = roleRepository.findByRoleName("ROLE_USER").orElseThrow(() -> new EntityNotFoundException("This role doesn't exist"));

        if (user.getRoles() == null) {
            user.setRoles(new ArrayList<>());
        }
        user.getRoles().add(userRole);
        return user.getRoles();
    }

/*    private void buildEmailMessage(User user) {
        String toAddress = user.getAuthenticationInfo().getEmail();
        emailService.sendRegistrationConfirmationEmail(toAddress);
    }*/

    private User findByIdAndIsDeletedFalse(Long id) {
        return userRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new EntityNotFoundException("User could not be found"));
    }

    private boolean isAuthorized(User user, UserDetails userDetails) {
        String userEmail = user.getAuthenticationInfo().getEmail();
        String username = userDetails.getUsername();

        return userEmail.equalsIgnoreCase(username);
    }


    private String generateUsername(Long id) throws UsernameGenerationException {
        if (id < 10) {
            return  "user-0000" + id;
        } else if (id<100) {
            return  "user-000"+id;
        } else if (id<1000) {
            return  "user-00"+id;
        } else if (id<10000) {
            return   "user-0"+id;
        } else if (id<100000) {
            return   "user-"+id;
        }else {
            throw new UsernameGenerationException("Username generation failed. User's id more then expected");
        }
    }
}
