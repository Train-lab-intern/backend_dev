package com.trainlab.service.impl;

import com.trainlab.dto.UserCreateDto;
import com.trainlab.dto.UserDto;
import com.trainlab.dto.UserUpdateDto;
import com.trainlab.exception.IllegalRequestException;
import com.trainlab.exception.ObjectNotFoundException;
import com.trainlab.exception.UsernameGenerationException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
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
    public User create(UserCreateDto userCreateDto) throws UsernameGenerationException {
        User user = userMapper.toEntity(userCreateDto);
        checkUserEmailAndPasswordExist(user);
        setEncodedPassword(user);
        setDefaultRole(user);
        userRepository.saveAndFlush(user);

          user.setUsername(generateUsername(user.getId()));
            userRepository.save(user);

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

        if (isAuthorized(user, userDetails)) {
            return userMapper.toDto(user);
        }
        else throw new AccessDeniedException("Access denied");
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
            throw new IllegalRequestException("User with this email is already exists.");
        }
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

        return userEmail.equalsIgnoreCase(username);
    }


    private String generateUsername(Long id) throws UsernameGenerationException {
        String usrname;
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
