package com.trainlab.security.provider;

import com.trainlab.model.AuthenticationInfo;
import com.trainlab.model.Role;
import com.trainlab.model.User;
import com.trainlab.repository.RoleRepository;
import com.trainlab.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDetailsProvider implements UserDetailsService {

    private static final Logger log = Logger.getLogger(UserDetailsProvider.class);

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> searchResult = userRepository.findByAuthenticationInfoEmail(email);

        if (searchResult.isPresent()) {
            User user = searchResult.get();
            AuthenticationInfo authenticationInfo = user.getAuthenticationInfo();
            Integer roleId = user.getRoleId().getId();
            Role role = roleRepository.findById(roleId).orElseThrow(() -> new UsernameNotFoundException("Role not found"));

            log.info("Fetching user by username: " + email);
            log.info("User role: " + role.getRoleName());

            return new org.springframework.security.core.userdetails.User(
                    authenticationInfo.getEmail(),
                    authenticationInfo.getUserPassword(),
                    AuthorityUtils.createAuthorityList(role.getRoleName())
            );
        } else {
            throw new UsernameNotFoundException("No user found with email: " + email);
        }
    }
}
