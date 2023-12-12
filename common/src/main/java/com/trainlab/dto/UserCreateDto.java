package com.trainlab.dto;

import com.trainlab.validation.Email;
import com.trainlab.validation.ValidPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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

//    @NotBlank(message = "User name must not be null")
//    @Size(message = "User name must be between 2 and 256 characters", min = 2, max = 256)
//    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "User name should consist of uppercase and lowercase letters and numbers without spaces")
//    @Schema(example = "SvetaPiven", type = "string", description = "Username")
//    private String username;

    @NotBlank(message = "User email must not be null")
    @Size(message = "User email must be between 1 and 256 characters", min = 1, max = 256)
    @Email
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "trainlab@gmail.com",
            type = "string", description = "User email")
    private String email;

    @NotBlank(message = "User password must not be null")
    @Size(message = "User password must be between 8 and 256 characters", min = 8, max = 256)
    @ValidPassword
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "123456qW",
            type = "string", description = "User password")
    private String password;
}
