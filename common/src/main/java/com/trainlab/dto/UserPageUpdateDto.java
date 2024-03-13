package com.trainlab.dto;

import com.trainlab.Enum.eSpecialty;
import com.trainlab.Enum.eUserLevel;
import com.trainlab.validation.ValidName;
import com.trainlab.validation.ValidPassword;
import com.trainlab.validation.ValidPasswordNull;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Builder
@Schema(description = "UserPage update response")
public class UserPageUpdateDto {

    @ValidName(nullable = true)
    @Schema(example = "Ivan",
            type = "string", description = "Users name")
    private String username;

    @ValidName(nullable = true)
    @Schema( example = "Ivanov",
            type = "string", description = "Users surname")
    private String surname;


    private eSpecialty specialty;

    @Size(message = "User email must be between 8 and 256 characters", min = 8, max = 256)
    @Email(message = "Invalid email address")
    @Schema(example = "trainlab@gmail.com",
            type = "string", description = "User email")
    private String email;

    @Size(message = "User password must be between 8 and 256 characters", min = 8, max = 256)
    @ValidPasswordNull
    @Schema(example = "123456qW",
            type = "string", description = "User password")
    private String password;
}

