package com.trainlab.dto;

import com.trainlab.valid.ValidPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import static com.trainlab.util.IPUtil.IPv4_PATTERN;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
@Schema(description = "User Request")
public class UserCreateDto {

    @NotNull(message = "User name must not be null")
    @Size(message = "User name must be between 2 and 256 characters", min = 2, max = 256)
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "User name should consist of uppercase and lowercase letters and numbers without spaces")
    @Schema(example = "SvetaPiven", type = "string", description = "Username")
    private String username;

    @NotNull(message = "User email must not be null")
    @Size(message = "User email must be between 1 and 256 characters", min = 1, max = 257)
    @Pattern(regexp = "^[A-Za-z0-9._%+\\-']+@(" + IPv4_PATTERN + "|([A-Za-z0-9.-]+\\.[A-Za-z]{2,4}))$",
             message = "Invalid email address.")
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
