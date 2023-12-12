package com.trainlab.security.security;

import lombok.Value;

import java.time.Instant;

@Value
public class AccessToken {
    String value;
    Instant issuedAt;
    Instant expiresAt;
}
