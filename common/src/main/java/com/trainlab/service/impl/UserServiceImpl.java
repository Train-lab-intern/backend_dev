package com.trainlab.service.impl;

import com.trainlab.dto.UserCreateDto;
import com.trainlab.dto.UserDto;
import com.trainlab.dto.UserUpdateDto;
import com.trainlab.exception.ObjectNotFoundException;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final RandomValuesGenerator generator;

    private final UserMapper userMapper;

    private final PasswordEncode passwordEncode;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final EmailService emailService;

    @Override
    public User create(UserCreateDto userCreateDto) {
        User user = userMapper.toEntity(userCreateDto);
        setEncodedPassword(user);
        setDefaultRole(user);
        userRepository.saveAndFlush(user);
        buildEmailMessage(user);
        return user;
    }

    @Override
    public void activateUser(String userEmail) {
        User user = findByEmail(userEmail);

        if (user.getIsActive()) {
            throw new IllegalStateException("User with email " + userEmail + " is yet activate.");
        }

        user.setIsActive(true);
        user.setChanged(Timestamp.valueOf(LocalDateTime.now().withNano(0)));
        userRepository.saveAndFlush(user);
        log.info("User with email " + userEmail + " activate successfully!");
    }

    @Override
    public UserDto findAuthorizedUser(Long id, UserDetails userDetails) {
        User user = findByIdAndIsDeletedFalse(id);
        UserDto userDto = null;
        if (isAuthorized(user, userDetails)) {
            userDto = userMapper.toDto(user);
        }
        return userDto;
    }

    @Override
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAllByIsDeletedFalseOrderById();
        return users.stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public UserDto update(UserUpdateDto userUpdateDto, Long id, UserDetails userDetails) {
        UserDto userDto = findAuthorizedUser(id, userDetails);
        User user = userMapper.toEntity(userDto);

        User updated = userMapper.partialUpdateToEntity(userUpdateDto, user);
        setEncodedPassword(updated);

        return userMapper.toDto(userRepository.saveAndFlush(updated));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByAuthenticationInfoEmailAndIsDeletedFalse(email).orElseThrow(()
                -> new ObjectNotFoundException("User not found with email: " + email));
    }

    @Override
    public void changePassword(Long id, UserUpdateDto userUpdateDto) {
        User user = findByIdAndIsDeletedFalse(id);
        String toAddress = userUpdateDto.getEmail();

        String newPassword = generator.generateRandomPassword(8);
        String encodedPassword = passwordEncode.encodePassword(newPassword);
        user.getAuthenticationInfo().setUserPassword(encodedPassword);

        userRepository.saveAndFlush(user);
        emailService.sendNewPassword(toAddress, newPassword);
    }

    private void setEncodedPassword(User user) {
        String encodedPassword = passwordEncode.encodePassword(user.getAuthenticationInfo().getUserPassword());
        user.getAuthenticationInfo().setUserPassword(encodedPassword);
    }

    private void setDefaultRole(User user) {
        Role userRole = roleRepository.findByRoleName("ROLE_USER").orElseThrow(() -> new EntityNotFoundException("This role doesn't exist"));

        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }
        user.getRoles().add(userRole);
    }

    private void buildEmailMessage(User user) {
        String toAddress = user.getAuthenticationInfo().getEmail();
        emailService.sendRegistrationConfirmationEmail(toAddress);
    }

    private User findByIdAndIsDeletedFalse(Long id) {
        return userRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new EntityNotFoundException("User could not be found"));
    }

    private boolean isAuthorized(User user, UserDetails userDetails) {
        String userEmail = user.getAuthenticationInfo().getEmail();
        String username = userDetails.getUsername();
        if (userEmail.equalsIgnoreCase(username)) {
            return true;
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }
}
