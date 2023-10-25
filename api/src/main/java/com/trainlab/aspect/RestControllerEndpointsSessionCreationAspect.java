package com.trainlab.aspect;

import com.trainlab.service.SessionService;
import com.trainlab.util.RandomValuesGenerator;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class RestControllerEndpointsSessionCreationAspect {

    private final SessionService sessionService;

    private final RandomValuesGenerator randomValuesGenerator;

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void restControllerMethods() {
    }

    @After("restControllerMethods()")
    public void whatGoesAroundComesAround() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String sessionToken = randomValuesGenerator.uuidGenerator();
        sessionService.createSession(sessionToken, authentication);
    }
}

