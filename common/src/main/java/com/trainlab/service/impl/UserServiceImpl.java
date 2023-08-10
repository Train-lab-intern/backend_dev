package com.trainlab.service.impl;

import com.trainlab.dto.UserCreateRequestDto;
import com.trainlab.dto.UserUpdateRequestDto;
import com.trainlab.exception.IllegalRequestException;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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
    public User create(UserCreateRequestDto userCreateRequestDto) {
        User user = userMapper.toEntity(userCreateRequestDto);
        setEncodedPassword(user);
        setDefaultRole(user);
        userRepository.saveAndFlush(user);
        buildEmailMessage(user);
        return user;
    }

    private void setEncodedPassword(User user) {
        String encodedPassword = passwordEncode.encodePassword(user.getAuthenticationInfo().getUserPassword());
        user.getAuthenticationInfo().setUserPassword(encodedPassword);
    }

    @Override
    public void activateUser(String userEmail) {
        User user = userRepository.findByAuthenticationInfoEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User with email not found"));

        if (!user.isActive()) {
            user.setActive(true);
            user.setChanged(Timestamp.valueOf(LocalDateTime.now()));
            userRepository.saveAndFlush(user);
            log.info("User with email " + userEmail + " activate successfully!");
        } else {
            throw new IllegalStateException("User with email " + userEmail + " is yet activate.");
        }
    }

    @Override
    public User findById(Long id, UserDetails userDetails) {
        String userEmail = userDetails.getUsername();

        Optional<User> userOptional = userRepository.findByAuthenticationInfoEmail(userEmail);
        User loggedInUser = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!loggedInUser.getId().equals(id)) {
            throw new AccessDeniedException("Access denied");
        }

        return loggedInUser;
    }

    @Override
    public List<User> findAll() {
        List<User> users = userRepository.findAllByIsDeletedFalseOrderById();

        if (users == null) {
            throw new EntityNotFoundException("Users not found");
        }

        return users;
    }

    @Override
    public User update(UserUpdateRequestDto userUpdateRequestDto, Long id, UserDetails userDetails) {
        String email = userDetails.getUsername();

        User user = userRepository.findByAuthenticationInfoEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        if (!user.getId().equals(id)) {
            throw new AccessDeniedException("Access denied");
        }

        User updated = userMapper.partialUpdateToEntity(userUpdateRequestDto, user);
        setEncodedPassword(updated);
        return userRepository.saveAndFlush(updated);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByAuthenticationInfoEmail(email).orElseThrow(()
                -> new ObjectNotFoundException("User not found"));
    }

    public void changePassword(UserUpdateRequestDto userUpdateRequestDto, Long id) {
        User user = userCheck(id);
        String toAddress = userUpdateRequestDto.getEmail();

        String newPassword = generator.generateRandomPassword(8);
        String encodedPassword = passwordEncode.encodePassword(newPassword);
        user.getAuthenticationInfo().setUserPassword(encodedPassword);

        userRepository.saveAndFlush(user);
        emailService.sendNewPassword(toAddress, newPassword);
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

    private User userCheck(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User could not be found"));

        if (user.isDeleted()) {
            log.error("User is deleted (isDeleted = true)");
            throw new IllegalRequestException("User is deleted");
        }

        return user;
    }
}
