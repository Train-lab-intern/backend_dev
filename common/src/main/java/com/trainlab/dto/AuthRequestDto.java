package com.trainlab.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trainlab.validation.Email;
import com.trainlab.validation.ValidPassword;
import com.trainlab.validation.groups.Group1;
import com.trainlab.validation.groups.Group2;
import com.trainlab.validation.groups.Group3;
import com.trainlab.validation.validator.EmailValidator;
import com.trainlab.validation.validator.PasswordValidator;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
@GroupSequence(value = {Group1.class, Group2.class, Group3.class, AuthRequestDto.class})
@Builder
@Schema(description = "User auth request.")
public class AuthRequestDto {

    @NotBlank(message = "The email field is required.", groups = {Group1.class})
    @Size(message = "User email must be between 8 and 256 characters", min = 8, max = 256, groups = {Group3.class})
    @Email
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "trainlab@gmail.com",
            type = "string", description = "User Email")
    private String userEmail;

    @NotBlank(message = "The password field is required.", groups = {Group1.class})
    @ValidPassword
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "123456qW",
            type = "string", description = "User password")
    private String userPassword;

    @JsonIgnore
    public boolean isFieldsBlank() {
        return userEmail.isBlank() && userPassword.isBlank();
    }

    @JsonIgnore
    @AssertTrue(message = "Invalid email and password. The password must be typed in Latin letters, " +
            "consist of at least 8 characters and contain at least one lowercase and one uppercase character",
            groups = {Group2.class})
    public boolean isEmailAndPasswordValid() {
        return new EmailValidator().isValid(userEmail, null) || new PasswordValidator().isValid(userPassword, null);
    }
}
