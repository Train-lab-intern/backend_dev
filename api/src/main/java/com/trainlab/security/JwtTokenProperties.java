package com.trainlab.security;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.time.Duration;

// Changed
@Value
@ConfigurationProperties(prefix = "jwt")
public class JwtTokenProperties {

    String secret;

    Duration timeToLive;

}
