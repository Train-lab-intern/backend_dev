package com.trainlab.util;

import com.trainlab.exception.UsernameGenerationException;
import org.springframework.stereotype.Component;

@Component
public class UsernameGenerator {
    public String generate(Long id) {
        return "user-"+ id;
    }
}
