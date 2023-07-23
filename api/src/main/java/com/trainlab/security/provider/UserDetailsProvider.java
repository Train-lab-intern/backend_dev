package com.trainlab.security.provider;

import com.trainlab.model.Role;
import com.trainlab.model.TrainlabUser;
import com.trainlab.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserDetailsProvider implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        TrainlabUser trainlabUser = userRepository.findByAuthenticationInfoEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User does not exist with this email"));

        Set<GrantedAuthority> authorities = trainlabUser.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toSet());

        log.info("Fetching user by username: " + email);
        log.info("User role: " + trainlabUser.getRoles().stream().findFirst().map(Role::getRoleName).orElse(null));

        return new org.springframework.security.core.userdetails.User(
                email,
                trainlabUser.getAuthenticationInfo().getUserPassword(),
                authorities
        );
    }
}