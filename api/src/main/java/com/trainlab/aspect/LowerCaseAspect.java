package com.trainlab.aspect;

import com.trainlab.dto.AuthRequestDto;
import com.trainlab.dto.UserCreateDto;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LowerCaseAspect {

    private static String userEmail;
    @Pointcut(value = "execution(org.springframework.http.ResponseEntity<com.trainlab.security.dto.AuthResponseDto> com.trainlab.controller.AuthenticationController.loginUser(com.trainlab.dto.AuthRequestDto,..))")
    public void isLoginUser() {
    }

    @Pointcut(value = "execution(* com.trainlab.controller.AuthenticationController.createUser(com.trainlab.dto.UserCreateDto,..))")
    public void isCreateUser() {
    }

    @Before(value = "isCreateUser()" +
            "&& args(userCreateDto,..)")
    public void toLowerCase(UserCreateDto userCreateDto) {
        if (userCreateDto.getEmail() != null) {
            userEmail = userCreateDto.getEmail();
            userCreateDto.setEmail(userEmail.toLowerCase());
        }
    }

    @Before(value = "isLoginUser()" +
            "&& args(authRequestDto,..)")
    public void toLowerCase(AuthRequestDto authRequestDto) {
        if (authRequestDto != null) {
            userEmail = authRequestDto.getUserEmail();
            authRequestDto.setUserEmail(userEmail);
        }
    }
}
