package com.trainlab.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.trainlab.security.principal.AccountPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private final JwtTokenProperties jwtProps;
    private final Algorithm jwtAlgorithm;

    public AccessToken generate(AccountPrincipal principal) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(jwtProps.getTimeToLive());

        String tokenValue = JWT.create()
                .withHeader(Map.of(
                        "typ", "JWT",
                        "alg", jwtAlgorithm.getName()))
                .withJWTId(UUID.randomUUID().toString())
                .withSubject(String.valueOf(principal.getId()))
                .withClaim("role", principal.getRole())
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .sign(jwtAlgorithm);

        return new AccessToken(tokenValue, principal, issuedAt, expiresAt);
    }
}
