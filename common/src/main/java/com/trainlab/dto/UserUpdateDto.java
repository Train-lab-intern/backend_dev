package com.trainlab.dto;

import com.trainlab.enums.Speciality;
import com.trainlab.enums.UserLevel;
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

    @Schema(example = "Ivan",type = "string",description = "First Name")
    @Size(message = "User first name must be between 1 and 256 characters", min = 1, max = 256)
    private String firstname;

    @Schema(example = "Ivanov", type = "string", description =  "Second Name")
    @Size(message = "User second name must be between 1 and 256 characters", min = 1, max = 256)
    private  String secondname;

    private UserLevel userlevel;

    private Speciality speciality;
}

