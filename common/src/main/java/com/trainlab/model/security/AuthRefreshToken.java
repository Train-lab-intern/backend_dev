package com.trainlab.model.security;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRefreshToken {
    String refreshToken;
    String fingerprint;
}
