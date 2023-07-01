package com.trainlab.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@AllArgsConstructor
@Controller
public class TestController {
    @GetMapping("/hello")
    public ResponseEntity<String> testHello(){
        return ResponseEntity.ok("Hello world!");
    }
}
