package com.trainlab.dto;

import com.trainlab.validation.ValidPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Builder
@Schema(description = "User Update Response")
public class UserUpdateDto {

    @Schema(example = "SvetaPiven", type = "string", description = "Username")
    private String username;

    @Size(message = "User email must be between 8 and 256 characters", min = 8, max = 256)
    @Email(message = "Invalid email address")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "trainlab@gmail.com",
            type = "string", description = "User email")
    private String email;

    @Size(message = "User password must be between 8 and 256 characters", min = 8, max = 256)
    @ValidPassword
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "123456qW",
            type = "string", description = "User password")
    private String password;
}

