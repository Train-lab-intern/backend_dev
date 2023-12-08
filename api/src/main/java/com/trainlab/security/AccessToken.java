package com.trainlab.security;

import com.trainlab.security.principal.AccountPrincipal;
import lombok.Value;

import java.time.Instant;

@Value
public class AccessToken {
    String value;
    AccountPrincipal principal;
    Instant issuedAt;
    Instant expiresAt;
}
