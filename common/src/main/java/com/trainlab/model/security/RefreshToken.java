package com.trainlab.model.security;

import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
public class RefreshToken {
    UUID value;
    Instant issuedAt;
    Instant expiredAt;
}
