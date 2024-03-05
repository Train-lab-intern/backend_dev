package com.trainlab.configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.trainlab.filter.AccessTokenAuthenticationFilter;
import com.trainlab.security.TokenProvider;
import com.trainlab.security.model.JwtTokenProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfiguration {
    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(6);
    }

/*    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        return new ProviderManager(provider);
    }*/

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, TokenProvider tokenProvider)
            throws Exception {
        return http
                    .httpBasic().disable()
                    .formLogin().disable()
                    .csrf().disable()
                    .sessionManagement(sessionManagement ->
                            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(config -> config
                            .requestMatchers("/v3/api-docs/**", "/v2/api-docs", "/configuration/ui/**",
                                "/swagger-resources/**", "/configuration/security/**", "/swagger-ui/**",
                                "/webjars/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/front/**").permitAll()
                            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                            .requestMatchers("/api/v1/**").permitAll()
                            .requestMatchers("/api/v1/users").permitAll()
                            .requestMatchers("/api/v1/roles").permitAll()
                            .requestMatchers("/api/v1/users/register").permitAll()
                            .requestMatchers("/api/v1/users/change-password/**").permitAll()
                            .requestMatchers("/api/v1/tests").permitAll()
                            .requestMatchers("/api/v1/users/**").hasAnyRole("USER", "ADMIN")
                            .requestMatchers("/api/v1/admin/users/**").hasAnyRole("ADMIN")
                            .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                            .requestMatchers("/api/v1/auth/**").hasAnyRole("USER", "ADMIN")
                            .anyRequest().authenticated()
                    )
                    .addFilterAfter(new AccessTokenAuthenticationFilter(tokenProvider), BasicAuthenticationFilter.class)
                    .build();
    }

    @Bean
    public Algorithm jwtAlgorithm(JwtTokenProperties jwtTokenProperties) {
        return Algorithm.HMAC256(jwtTokenProperties.getSecret());
    }

    @Bean
    public JWTVerifier jwtVerifier(Algorithm algorithm) {
        return JWT.require(algorithm).build();
    }
}
