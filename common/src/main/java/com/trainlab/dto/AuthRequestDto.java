package com.trainlab.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuthRequestDto {

    @Schema(example = "trainlab@gmail.com", type = "string", description = "User Email")
    private String userEmail;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "123456qW", type = "string", description = "User password")
    private String userPassword;
}
