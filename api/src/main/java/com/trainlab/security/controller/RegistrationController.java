package com.trainlab.security.controller;

import com.trainlab.dto.request.UserRequest;
import com.trainlab.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rest")
public class RegistrationController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRequest registrationRequest) {
        try {
            userService.create(registrationRequest);
            return ResponseEntity.ok("Пользователь успешно зарегистрирован");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/complete-registration")
    public ResponseEntity<String> completeRegistration(@RequestParam("userEmail") String userEmail) {
        try {
            userService.activateUser(userEmail);
            return ResponseEntity.ok("Регистрация завершена!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
