package com.trainlab.security.model;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.time.Duration;

@Value
@ConfigurationProperties(prefix = "jwt")
public class JwtTokenProperties {

    String secret;

    Duration timeToLive;

}
