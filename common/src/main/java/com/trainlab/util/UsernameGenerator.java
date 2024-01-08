package com.trainlab.util;

import com.trainlab.exception.UsernameGenerationException;
import org.springframework.stereotype.Component;

@Component
public class UsernameGenerator {
    public String generate(Long id) {
        if (id < 10) {
            return  "user-0000" + id;
        } else if (id<100) {
            return  "user-000"+id;
        } else if (id<1000) {
            return  "user-00"+id;
        } else if (id<10000) {
            return   "user-0"+id;
        } else if (id<100000) {
            return   "user-"+id;
        }else {
            throw new UsernameGenerationException("Username generation failed. User's id more then expected");
        }
    }
}
