package com.trainlab.util.password;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomPasswordEncoder {

    private final PasswordEncoder passwordEncoder;
    private final PasswordProvider passwordProvider;

    public String encodePassword(String password) {
        String passwordWithSalt = password + passwordProvider.getPasswordSalt();
        return passwordEncoder.encode(passwordWithSalt);
    }

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword + passwordProvider.getPasswordSalt(), encodedPassword);
    }
}
