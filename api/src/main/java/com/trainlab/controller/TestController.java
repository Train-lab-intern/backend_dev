package com.trainlab.controller;

import com.trainlab.model.TestEntity;
import com.trainlab.repository.TestEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class TestController {
    private final TestEntityRepository testRepository;

    @GetMapping("/hello1")
    public ResponseEntity<String> testHello() {
        return ResponseEntity.ok("Hello world!");
    }

    @GetMapping("/hello")
    public ResponseEntity<String> getHelloContent() {
        TestEntity testEntity = testRepository.findById(1L).orElse(null);
        if (testEntity != null) {
            return ResponseEntity.ok("Hello World! Content: " + testEntity.getHello());
        } else {
            return ResponseEntity.ok("Hello World!");
        }
    }

    @GetMapping("/test_hello")
    public ResponseEntity<Optional<TestEntity>> testHelloContent() {
        Optional<TestEntity> testEntity = testRepository.findById(1L);
        return new ResponseEntity<>(testEntity, HttpStatus.OK);
    }
}
