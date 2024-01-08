package com.trainlab.util;

import com.trainlab.util.password.PasswordProvider;
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
