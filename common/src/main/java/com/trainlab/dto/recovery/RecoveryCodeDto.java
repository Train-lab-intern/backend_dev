package com.trainlab.dto.recovery;

import com.trainlab.validation.Email;
import com.trainlab.validation.groups.Group1;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@GroupSequence(value = {Group1.class, RecoveryCodeDto.class})
@Schema(description = "The user's email and code for verification")
public class RecoveryCodeDto {

    @NotBlank(message = "Email must be specified", groups = Group1.class)
    @Email
    @Schema(example = "vladthedev7@gmail.com", description = "User email", type = "String")
    String email;

    @NotBlank(message = "Code must be specified", groups = Group1.class)
    @Pattern(message = "Invalid code", regexp = "^\\d{10}$")
    @Schema(example = "2345612321", description = "Verification code", type = "String")
    String code;
}
