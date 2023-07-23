package com.trainlab.service.impl;

import com.trainlab.dto.request.UserRequest;
import com.trainlab.mapper.UserMapper;
import com.trainlab.model.Role;
import com.trainlab.model.TrainlabUser;
import com.trainlab.repository.RoleRepository;
import com.trainlab.repository.UserRepository;
import com.trainlab.service.EmailService;
import com.trainlab.service.UserService;
import com.trainlab.utils.PasswordEncode;
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
    public TrainlabUser create(UserRequest userRequest) {
        TrainlabUser trainlabUser = userMapper.toEntity(userRequest);

        String encodedPassword = passwordEncode.encodePassword(trainlabUser.getAuthenticationInfo().getUserPassword());
        trainlabUser.getAuthenticationInfo().setUserPassword(encodedPassword);

        Role userRole = roleRepository.findByRoleName("ROLE_USER").orElseThrow(() -> new EntityNotFoundException("This role doesn't exist"));
        if (trainlabUser.getRoles() == null) {
            trainlabUser.setRoles(new HashSet<>());
        }
        trainlabUser.getRoles().add(userRole);

        userRepository.save(trainlabUser);

        String toAddress = trainlabUser.getAuthenticationInfo().getEmail();
        String subject = "Подтверждение регистрации";

        String encodedEmail = URLEncoder.encode(toAddress, StandardCharsets.UTF_8);
        String message = "Спасибо за регистрацию! Пожалуйста, перейдите по ссылке ниже, чтобы завершить регистрацию:\n" +
                "http://localhost:8080/rest/users/complete-registration?userEmail=" + encodedEmail +
//                "http://" + dbHost + "/rest/complete-registration?userEmail=" + encodedEmail +
                "\nС наилучшими пожеланиями,\nКоманда Trainlab";
        emailService.sendRegistrationConfirmationEmail(toAddress, subject, message);

        return trainlabUser;
    }

    @Override
    public void activateUser(String userEmail) {
        TrainlabUser trainlabUser = userRepository.findByAuthenticationInfoEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с таким email не найден"));

        if (!trainlabUser.isActive()) {

            trainlabUser.setActive(true);
            userRepository.save(trainlabUser);

            log.info("Пользователь с email " + userEmail + " успешно активирован!");
        } else {
            throw new IllegalStateException("Пользователь с email " + userEmail + " уже активирован.");
        }
    }
}
