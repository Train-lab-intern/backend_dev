package com.trainlab.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.UUID;

@Component
public class RandomValuesGenerator {

    public String uuidGenerator() {
        return UUID.randomUUID().toString();
    }

    public String generateRandomPassword(int len)    {
        final String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        final String digits = "0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        sb.append(upperCase.charAt(random.nextInt(upperCase.length())));
        sb.append(lowerCase.charAt(random.nextInt(lowerCase.length())));

        for (int i = 2; i < len; i++) {
            int category = random.nextInt(3);
            if (category == 0) {
                sb.append(lowerCase.charAt(random.nextInt(lowerCase.length())));
            } else if (category == 1) {
                sb.append(upperCase.charAt(random.nextInt(upperCase.length())));
            } else {
                sb.append(digits.charAt(random.nextInt(digits.length())));
            }
        }

        return sb.toString();
    }
}
