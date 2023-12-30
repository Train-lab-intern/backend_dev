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
import lombok.experimental.FieldNameConstants;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
@GroupSequence(value = {Group1.class, Group2.class, Group3.class, UserCreateDto.class})
@Schema(description = "User Request")
@FieldNameConstants
public class UserCreateDto {

    @NotBlank(message = "The email field is required", groups = {Group1.class})
    @Size(message = "User email must be between 8 and 256 characters", min = 8, max = 256, groups = {Group3.class})
    @Email
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "trainlab@gmail.com",
            type = "string", description = "User email.")
    private String email;

    @NotBlank(message = "The password field is required", groups = {Group1.class})
    @ValidPassword
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "123456qW",
            type = "string", description = "User password.")
    private String password;

    @JsonIgnore
    public boolean isFieldsBlank() {
        return email.isBlank() && password.isBlank();
    }

    @JsonIgnore
    @AssertTrue(message = "Invalid email and password. The password must be typed in Latin letters, " +
                        "consist of at least 8 characters and contain at least one lowercase and one uppercase character",
    groups = {Group2.class})
    public boolean isEmailAndPasswordValid() {
        return new EmailValidator().isValid(email, null) || new PasswordValidator().isValid(password, null);
    }
}
//    @NotBlank(message = "User name must not be null")
//    @Size(message = "User name must be between 2 and 256 characters", min = 2, max = 256)
//    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "User name should consist of uppercase and lowercase letters and numbers without spaces")
//    @Schema(example = "SvetaPiven", type = "string", description = "Username")
//    private String username;