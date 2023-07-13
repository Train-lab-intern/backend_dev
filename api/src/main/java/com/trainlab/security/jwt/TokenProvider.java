package com.trainlab.security.jwt;

import com.trainlab.security.config.JwtConfiguration;
import com.trainlab.security.provider.UserDetailsProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    private static final Logger log = Logger.getLogger(TokenProvider.class);

    public static final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.HS512;

    public static final String CREATE_VALUE = "created";

    public static final String ROLES = "roles";

    public static final String JWT = "JWT";

    public static final Integer LIFETIME = 1000;

    private final JwtConfiguration jwtConfiguration;

    private final UserDetailsProvider userDetailsProvider;

    private String generateToken(Map<String, Object> claims) {

        log.info("Generating with secret key: " + jwtConfiguration.getSecret());

        return Jwts
                .builder()
                .setHeader(generateJWTHeaders())
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(ALGORITHM, jwtConfiguration.getSecret())
                .compact();


    }

    public String generateToken(UserDetails userDetails) {

        Map<String, Object> claims = new HashMap<>();

        claims.put(Claims.SUBJECT, userDetails.getUsername());
        log.info("Generated user: " + userDetails.getUsername());

        claims.put(CREATE_VALUE, generateCurrentDate());
        log.info("Generated created time: " + generateCurrentDate());

        claims.put(ROLES, getEncryptedRoles(userDetails));
        log.info("Generated roles: " + userDetails.getAuthorities());

        return generateToken(claims);
    }

    private List<String> getEncryptedRoles(UserDetails userDetails) {
        return userDetails.getAuthorities().
                stream()
                .map(GrantedAuthority::getAuthority)
                .map(String::toLowerCase).toList();
    }

    private Map<String, Object> generateJWTHeaders() {
        Map<String, Object> jwtHeaders = new LinkedHashMap<>();
        jwtHeaders.put("typ", JWT);
        jwtHeaders.put("alg", ALGORITHM.getValue());

        return jwtHeaders;
    }

    private Date generateExpirationDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MILLISECOND, jwtConfiguration.getExpiration() * LIFETIME);

        log.info("Token expiration: " + jwtConfiguration.getExpiration() * LIFETIME);

        return calendar.getTime();
    }


    public String extractToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts
                .parser()
                .setSigningKey(jwtConfiguration.getSecret())
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = this.getExpirationDateFromToken(token);
        return expiration.before(this.generateCurrentDate());
    }

    private Date generateCurrentDate() {
        return new Date();
    }


    public Authentication getAuthentication(String token) {

        log.info("Authentication...");

        Claims claims = Jwts.parser().setSigningKey(jwtConfiguration.getSecret()).parseClaimsJws(token).getBody();
        String username = claims.getSubject();
        UserDetails userDetails = userDetailsProvider.loadUserByUsername(username);

        log.info("User details: " + userDetails);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
