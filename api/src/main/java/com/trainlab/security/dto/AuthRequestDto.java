package com.trainlab.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequestDto {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "trainlab@gmail.com", type = "string", description = "User Email")
    private String userEmail;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "123456qW", type = "string", description = "User password")
    private String userPassword;
}
