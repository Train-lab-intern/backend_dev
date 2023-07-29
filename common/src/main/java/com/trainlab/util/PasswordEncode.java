package com.trainlab.util;

import com.trainlab.configuration.JwtConfigurationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordEncode {

    private final PasswordEncoder passwordEncoder;

    private final JwtConfigurationProvider jwtConfigurationProvider;

    public String encodePassword(String password) {
        String passwordWithSalt = password + jwtConfigurationProvider.getPasswordSalt();
        return passwordEncoder.encode(passwordWithSalt);
    }
}
