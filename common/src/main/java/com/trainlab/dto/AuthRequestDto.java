package com.trainlab.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trainlab.validation.Email;
import com.trainlab.validation.ValidPassword;
import com.trainlab.validation.groups.Group1;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
@GroupSequence(value = {Group1.class, AuthRequestDto.class})
@Builder
@Schema(description = "User auth request.")
public class AuthRequestDto {

    @NotBlank(message = "The email field is required.", groups = {Group1.class})
    @Size(message = "User email must be between 1 and 256 characters", min = 1, max = 256)
    @Email
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "trainlab@gmail.com",
            type = "string", description = "User Email")
    private String userEmail;

    @NotBlank(message = "The password field is required.", groups = {Group1.class})
    @Size(message = "User password must be between 8 and 256 characters", min = 8, max = 256)
    @ValidPassword
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "123456qW",
            type = "string", description = "User password")
    private String userPassword;

    @JsonIgnore
    public boolean isValid() {
        return userEmail.isBlank() && userPassword.isBlank();
    }
}
