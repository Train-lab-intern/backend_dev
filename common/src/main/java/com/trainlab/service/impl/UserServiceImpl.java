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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncode passwordEncode;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmailService emailService;

//    @Value("${DB_HOST}")
//    private String dbHost;

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
                "http://localhost:8080/rest/users/complete-registration?userEmail=" + encodedEmail +
//                "http://" + dbHost + "/rest/complete-registration?userEmail=" + encodedEmail +
                "\nС наилучшими пожеланиями,\nКоманда Trainlab";
        emailService.sendRegistrationConfirmationEmail(toAddress, subject, message);

        return user;
    }

    @Override
    public void activateUser(String userEmail) {
        User user = userRepository.findByAuthenticationInfoEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с таким email не найден"));

        if (!user.isActive()) {

            user.setActive(true);
            userRepository.save(user);

            log.info("Пользователь с email " + userEmail + " успешно активирован!");
        } else {
            throw new IllegalStateException("Пользователь с email " + userEmail + " уже активирован.");
        }
    }
}
