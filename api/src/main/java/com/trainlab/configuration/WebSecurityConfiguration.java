package com.trainlab.configuration;

import com.auth0.jwt.algorithms.Algorithm;
import com.trainlab.filter.JwtTokenFilter;
import com.trainlab.security.JwtTokenProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    private final UserDetailsService userDetailsService;

    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(6);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        return new ProviderManager(provider);
    }

    // Changed
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        return http
                    .httpBasic().disable()
                    .formLogin().disable()
                    .csrf().disable()
                    .sessionManagement(sessionManagement ->
                            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                    .authorizeHttpRequests(config -> config
                            .requestMatchers("/v3/api-docs/**", "/v2/api-docs", "/configuration/ui/**",
                                "/swagger-resources/**", "/configuration/security/**", "/swagger-ui/**", "/swagger-ui.html#",
                                "/webjars/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/front/**").permitAll()
                            .requestMatchers("/swagger-ui/**", "/swagger-ui.html").permitAll()
                            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                            .requestMatchers("/api/v1/**").permitAll()
                            .requestMatchers("/api/v1/users").permitAll()
                            .requestMatchers("/api/v1/roles").permitAll()
                            .requestMatchers("/api/v1/users/register").permitAll()
                            .requestMatchers("/api/v1/users/complete-registration").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/v1/auth").permitAll()
                            .requestMatchers("/api/v1/users/change-password/**").permitAll()
                            .requestMatchers("/api/v1/users/**").hasAnyRole("USER", "ADMIN")
                            .requestMatchers("/api/v1/admin/users/**").hasAnyRole("ADMIN")
                            .anyRequest().authenticated()
                    ).build();
    }

    @Bean
    public Algorithm jwtAlgorithm(JwtTokenProperties jwtTokenProperties) {
        return Algorithm.HMAC256(jwtTokenProperties.getSecret());
    }
}
