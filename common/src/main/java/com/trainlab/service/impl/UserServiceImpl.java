package com.trainlab.service.impl;

import com.trainlab.dto.request.UserRequest;
import com.trainlab.mapper.UserMapper;
import com.trainlab.model.Role;
import com.trainlab.model.User;
import com.trainlab.repository.RoleRepository;
import com.trainlab.repository.UserRepository;
import com.trainlab.service.UserService;
import com.trainlab.utils.PasswordEncode;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final PasswordEncode passwordEncode;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public User create(UserRequest userRequest) {
        User user = userMapper.toEntity(userRequest);

        String encodedPassword = passwordEncode.encodePassword(user.getAuthenticationInfo().getUserPassword());
        user.getAuthenticationInfo().setUserPassword(encodedPassword);

        Role userRole = null;
        try {
            userRole = roleRepository.findByRoleName("ROLE_USER").orElseThrow(() -> new EntityNotFoundException("This role doesn't exist"));
        } catch (EntityNotFoundException e) {
            log.error("This role doesn't exist. " + e.getMessage());
        }
        if (userRole != null) {
            if (user.getRoles() == null) {
                user.setRoles(new HashSet<>());
            }
            user.getRoles().add(userRole);
        }

        userRepository.save(user);

        return user;

//        try {
//            user = userMapper.toEntity(userRequest);
//        } catch (ForbiddenChangeException e) {
//            log.error("Wrong mapping for entity. " + e.getMessage());
//            throw new ForbiddenChangeException(e.getMessage());
//        }
//
//        Role userRole = null;
//        try {
//            userRole = roleRepository.findByRoleName("USER").orElseThrow(() -> new EntityNotFoundException("This role doesn't exist"));
//        } catch (EntityNotFoundException e) {
//            logger.error("This role doesn't exist. " + e.getMessage());
//        }
//        if (userRole != null) {
//            user.getRoles().add(userRole);
//        }
//
//        String encodedPassword = passwordEncode.encodePassword(user.getAuthenticationInfo().getPassword());
//        user.getAuthenticationInfo().setPassword(encodedPassword);
//
//        userRepository.save(user);
//
//        try {
//            UserBalance userBalance = new UserBalance();
//            userBalance.setUser(user);
//            userBalance.setBalance(BigDecimal.ZERO);
//            userBalanceRepository.save(userBalance);
//        } catch (EntityNotFoundException e) {
//            logger.error("User balance wasn't added. " + e.getMessage());
//            throw new EntityNotFoundException(e.getMessage());
//        }
//        return user;
    }

}
