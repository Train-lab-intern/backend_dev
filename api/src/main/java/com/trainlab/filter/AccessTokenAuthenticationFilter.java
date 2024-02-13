package com.trainlab.filter;

import com.trainlab.security.TokenProvider;
import com.trainlab.principal.AccountPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.testcontainers.shaded.com.google.common.net.HttpHeaders;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class AccessTokenAuthenticationFilter extends OncePerRequestFilter {

    private final static String TOKEN_PREFIX = "Bearer ";
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
            String tokenValue = authHeader.substring(TOKEN_PREFIX.length());
            AccountPrincipal principal = tokenProvider.authenticate(tokenValue);
            List<SimpleGrantedAuthority> authorities = principal.getRole().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getRoleName())).toList();

            authenticate(principal, authorities);
        }

        filterChain.doFilter(request, response);
    }

    private void authenticate(AccountPrincipal principal, List<SimpleGrantedAuthority> authorities) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, authorities);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}
