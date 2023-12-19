package com.trainlab.util;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "config.password-salt")
@RequiredArgsConstructor
public class PasswordProviderImpl implements PasswordProvider {

    private final String passwordSalt;

    @Override
    public String getPasswordSalt() {
        return passwordSalt;
    }
}
