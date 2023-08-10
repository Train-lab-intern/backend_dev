package com.trainlab.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "config")
public class JwtConfiguration implements JwtConfigurationProvider {

    private String secret;

    private Integer expiration;

    private String passwordSalt;

}
