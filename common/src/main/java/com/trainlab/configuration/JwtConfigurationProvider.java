package com.trainlab.configuration;

public interface JwtConfigurationProvider {
    String getSecret();

    Integer getExpiration();

    String getPasswordSalt();
}
