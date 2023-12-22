package com.trainlab.util;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "config")
@Value
public class PasswordProviderImpl implements PasswordProvider {

    String passwordSalt;

    @Override
    public String getPasswordSalt() {
        return passwordSalt;
    }
}
