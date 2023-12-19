package com.trainlab.security.model;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Value
@ConfigurationProperties(prefix = "refresh-token")
public class RefreshTokenProperties {
    Duration timeToLive;
}
