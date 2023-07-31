package com.trainlab.service.impl;

import com.trainlab.dto.request.UserRequest;
import com.trainlab.mapper.UserMapper;
import com.trainlab.model.Role;
import com.trainlab.model.User;
import com.trainlab.repository.RoleRepository;
import com.trainlab.repository.UserRepository;
import com.trainlab.service.EmailService;
import com.trainlab.service.UserService;
import com.trainlab.util.PasswordEncode;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncode passwordEncode;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmailService emailService;

    @Override
    @Transactional
    public User create(UserRequest userRequest) {
        User user = userMapper.toEntity(userRequest);
        setEncodedPassword(user);
        setDefaultRole(user);
        userRepository.save(user);
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
            userRepository.save(user);
            log.info("User with email " + userEmail + " activate successfully!");
        } else {
            throw new IllegalStateException("User with email " + userEmail + " is yet activate.");
        }
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
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
    @Transactional
    public User update(UserRequest userRequest, Long id) {
        User user = findById(id);
        User updated = userMapper.partialUpdateToEntity(userRequest, user);
        setEncodedPassword(updated);
        return userRepository.saveAndFlush(updated);
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
        String emailSubject = "Подтверждение регистрации";
        String encodedEmail = URLEncoder.encode(toAddress, StandardCharsets.UTF_8);
        String message = "Спасибо за регистрацию! Пожалуйста, перейдите по ссылке ниже, чтобы завершить регистрацию:\n" +
                "https://test.app.it-roast.com/api/v1/users/complete-registration?userEmail=" + encodedEmail +
                "\nС наилучшими пожеланиями,\nКоманда Trainlab";
        emailService.sendRegistrationConfirmationEmail(toAddress, emailSubject, message);
    }

}
