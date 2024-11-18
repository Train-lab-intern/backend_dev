package com.trainlab.dto.recovery;

import com.trainlab.validation.Email;
import com.trainlab.validation.groups.Group1;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
@GroupSequence(value = {Group1.class, EmailRequestDto.class})
@Builder
@Schema(description = "The user's email address for password recovery")
public class EmailRequestDto {

    @NotBlank(message = "Email must be specified", groups = Group1.class)
    @Email
    @Schema(example = "vladthedev7@gmail.com", description = "User email", type = "String")
    private String email;
}
