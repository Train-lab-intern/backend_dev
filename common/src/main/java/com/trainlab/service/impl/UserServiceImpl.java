package com.trainlab.service.impl;

import com.trainlab.dto.*;
import com.trainlab.exception.IllegalRequestException;
import com.trainlab.exception.ObjectNotFoundException;
import com.trainlab.mapper.UserMapper;
import com.trainlab.model.Role;
import com.trainlab.model.User;
import com.trainlab.repository.RoleRepository;
import com.trainlab.repository.UserRepository;
import com.trainlab.service.EmailService;
import com.trainlab.service.UserService;
import com.trainlab.util.UsernameGenerator;
import com.trainlab.util.password.CustomPasswordEncoder;
import com.trainlab.util.RandomValuesGenerator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private static final String DEFAULT_ROLE = "ROLE_USER";
    private final RandomValuesGenerator generator;
    private final UsernameGenerator usernameGenerator;
    private final UserMapper userMapper;
    private final CustomPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmailService emailService;

    @Override
    public User create(UserCreateDto userCreateDto) {
        User user = userMapper.toEntity(userCreateDto);

        checkIsEmailExist(user);
        setEncodedPassword(user);
        setDefaultRole(user);

        user.setGeneratedName("user-");
        userRepository.saveAndFlush(user);
        user.setGeneratedName(usernameGenerator.generate(user.getId()));

        return userRepository.saveAndFlush(user);
    }

    private void checkIsEmailExist(User user) {
        Optional<User> userByEmail = userRepository.findByAuthenticationInfoEmail(
                user.getAuthenticationInfo().getEmail()
        );
        if (userByEmail.isPresent())
            throw new IllegalRequestException("User with this email is already exists");
    }

    private  void  checkEmail(String email){
        Optional<User> userByEmail = userRepository.findByAuthenticationInfoEmail(
                email
        );
        if (userByEmail.isPresent())
            throw new IllegalRequestException("User with this email is already exists");
    }

    private void setEncodedPassword(User user) {
        String encodedPassword = passwordEncoder.encodePassword(user.getAuthenticationInfo().getUserPassword());
        user.getAuthenticationInfo().setUserPassword(encodedPassword);
    }

    private void setDefaultRole(User user) {
        Role userRole = roleRepository.findByRoleName(DEFAULT_ROLE).orElseThrow(
                () -> new EntityNotFoundException("This role doesn't exist")
        );

        if (user.getRoles() == null)
            user.setRoles(new ArrayList<>());

        user.getRoles().add(userRole);
    }

    @Override
    public User findUserByAuthenticationInfo(AuthRequestDto authRequestDto) {
        User user = userRepository.findByAuthenticationInfoEmailAndIsDeletedFalse(authRequestDto
                .getUserEmail().toLowerCase())
                .orElseThrow(() -> new ObjectNotFoundException("Invalid login or password"));

        boolean isPasswordMatches = passwordEncoder.matches(
                authRequestDto.getUserPassword(),
                user.getAuthenticationInfo().getUserPassword()
        );

        if (!isPasswordMatches)
            throw new ObjectNotFoundException("Invalid login or password");

        return user;
    }

    @Override
    public User findAuthorizedUser(Long id) {
        return userRepository.findByIdAndIsDeletedFalse(id).orElseThrow(
                () -> new ObjectNotFoundException("User could not be found")
        );
    }

    @Override
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAllByIsDeletedFalseOrderById();
        return users.stream()
                .map(userMapper::toDto)
                .toList();
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
    public UserPageDto update(UserPageUpdateDto userUpdateDto, Long id) {
        User user = findAuthorizedUser(id);

        if(userUpdateDto.getUsername() != null ){
            user.setUsername(userUpdateDto.getUsername());
        }
        if(userUpdateDto.getSurname() != null){
            user.setSurname(userUpdateDto.getSurname());
        }
        if(userUpdateDto.getSpecialty() != null){
            user.setSpecialty(userUpdateDto.getSpecialty());
        }
        if(userUpdateDto.getEmail() != null){
            checkEmail(userUpdateDto.getEmail());
            user.getAuthenticationInfo().setEmail(userUpdateDto.getEmail());
        }
        if(userUpdateDto.getPassword() != null){
            String newPassword = userUpdateDto.getPassword();
            String encodedPassword = passwordEncoder.encodePassword(newPassword);
            user.getAuthenticationInfo().setUserPassword(encodedPassword);
        }

        userRepository.saveAndFlush(user);
        return userMapper.toUserPageDto(user);
    }
    @Override
    public void changePassword(UserUpdateDto userUpdateDto) {
        User user = userRepository.findByAuthenticationInfoEmailAndIsDeletedFalse(userUpdateDto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User could not be found"));

        String toAddress = userUpdateDto.getEmail();

        String newPassword = generator.generateRandomPassword(8);
        String encodedPassword = passwordEncoder.encodePassword(newPassword);
        user.getAuthenticationInfo().setUserPassword(encodedPassword);

        userRepository.saveAndFlush(user);
        emailService.sendNewPassword(toAddress, newPassword);
    }
    private boolean isAuthorized(User user, UserDetails userDetails) {
        String userEmail = user.getAuthenticationInfo().getEmail();
        String username = userDetails.getUsername();

        return userEmail.equalsIgnoreCase(username);
    }
}
