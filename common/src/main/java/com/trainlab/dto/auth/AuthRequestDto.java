package com.trainlab.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trainlab.validation.Email;
import com.trainlab.validation.ValidPassword;
import com.trainlab.validation.groups.Group1;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
@GroupSequence(value = {Group1.class, AuthRequestDto.class})
@Builder
@Schema(description = "User auth request")
public class AuthRequestDto {

    @NotBlank(groups = {Group1.class}, message = "The email field is required")
    @Email
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "trainlab@gmail.com",
            type = "string", description = "User Email")
    private String email;

    @NotBlank(groups = {Group1.class}, message = "The password field is required")
    @ValidPassword
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "123456qW",
            type = "string", description = "User password")
    private String password;

    @JsonIgnore
    public boolean isFieldsBlank() {
        return email.isBlank() && password.isBlank();
    }
}
