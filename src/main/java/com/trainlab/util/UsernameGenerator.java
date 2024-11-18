package com.trainlab.util;

import org.springframework.stereotype.Component;

@Component
public class UsernameGenerator {
    public String generate(Long id) {
        return "user-"+ id;
    }
}
