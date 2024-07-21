package com.trainlab.dto.recovery;

import lombok.*;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecoveryCodeDto {

    String email;
    String code;
}
