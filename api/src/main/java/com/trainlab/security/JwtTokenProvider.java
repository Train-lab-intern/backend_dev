package com.trainlab.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.trainlab.model.Role;
import com.trainlab.security.security.AccessToken;
import com.trainlab.security.principal.AccountPrincipal;
import com.trainlab.security.principal.UserPrincipal;
import com.trainlab.security.security.JwtTokenProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider implements TokenProvider {

    private final JwtTokenProperties jwtProps;
    private final Algorithm jwtAlgorithm;
    private final JWTVerifier jwtVerifier;

    public AccessToken generate(AccountPrincipal principal) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(jwtProps.getTimeToLive());

        List<String> roles = principal.getRole().stream()
                .map(Role::getRoleName)
                .toList();

        String tokenValue = JWT.create()
                .withHeader(Map.of(
                        "typ", "JWT",
                        "alg", jwtAlgorithm.getName()))
                .withJWTId(UUID.randomUUID().toString())
                .withSubject(String.valueOf(principal.getId()))
                .withClaim("role", roles)
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .sign(jwtAlgorithm);

        return new AccessToken(tokenValue, issuedAt, expiresAt);
    }

    public AccountPrincipal authenticate(String tokenValue) {
        try {
            DecodedJWT jwt = jwtVerifier.verify(tokenValue);
            Long accountId = jwt.getClaim("sub").as(Long.class);
            List<Role> roles = jwt.getClaim("role").asList(Role.class);
            return new UserPrincipal(accountId, roles);
        } catch (TokenExpiredException e) {
            throw new CredentialsExpiredException("JWT is expired", e);
        } catch (JWTVerificationException e) {
            throw new BadCredentialsException("JWT is invalid", e);
        }
    }
}
