package com.trainlab.service.impl;

import com.trainlab.dto.request.UserRequest;
import com.trainlab.mapper.UserMapper;
import com.trainlab.model.Role;
import com.trainlab.model.Session;
import com.trainlab.model.User;
import com.trainlab.repository.RoleRepository;
import com.trainlab.repository.SessionRepository;
import com.trainlab.repository.UserRepository;
import com.trainlab.service.EmailService;
import com.trainlab.service.SessionService;
import com.trainlab.service.UserService;
import com.trainlab.utils.PasswordEncode;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncode passwordEncode;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmailService emailService;

    @Value("${DB_HOST}")
    private String dbHost;

    @Override
    @Transactional
    public User create(UserRequest userRequest) {
        User user = userMapper.toEntity(userRequest);

        String encodedPassword = passwordEncode.encodePassword(user.getAuthenticationInfo().getUserPassword());
        user.getAuthenticationInfo().setUserPassword(encodedPassword);

        Role userRole = roleRepository.findByRoleName("ROLE_USER").orElseThrow(() -> new EntityNotFoundException("This role doesn't exist"));
        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }
        user.getRoles().add(userRole);

        userRepository.save(user);

        String toAddress = user.getAuthenticationInfo().getEmail();
        String subject = "Подтверждение регистрации";

        String encodedEmail = URLEncoder.encode(toAddress, StandardCharsets.UTF_8);
        String message = "Спасибо за регистрацию! Пожалуйста, перейдите по ссылке ниже, чтобы завершить регистрацию:\n" +
                "http://" + dbHost + "/complete-registration?userEmail=" + encodedEmail +
                "\nС наилучшими пожеланиями,\nКоманда Trainlab";
        emailService.sendRegistrationConfirmationEmail(toAddress, subject, message);

        return user;
    }
}
