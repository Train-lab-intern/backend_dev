package com.trainlab.dto;

import com.trainlab.valid.ValidPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
@Schema(description = "User Request")
public class UserCreateDto {

    @Schema(example = "SvetaPiven", type = "string", description = "Username")
    private String username;

    @NotNull(message = "User email must not be null")
    @Size(message = "User email must be between 1 and 256 characters", min = 1, max = 256)
    @Email(message = "Invalid email address")
    @Pattern(regexp = "^(.+)@(.+\\..+)$", message = "Invalid email address. The email should contain a dot (.) in the domain part.")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "trainlab@gmail.com",
            type = "string", description = "User email")
    private String email;

    @NotNull(message = "User password must not be null")
    @Size(message = "User password must be between 8 and 256 characters", min = 8, max = 256)
    @ValidPassword
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "123456qW",
            type = "string", description = "User password")
    private String password;
}
