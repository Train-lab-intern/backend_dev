package com.trainlab.dto.recovery;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordRecoveryMessage {
    private String message;
}
