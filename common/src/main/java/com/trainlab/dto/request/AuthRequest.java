package com.trainlab.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuthRequest {

  //  @Schema(example = "svetapiven93@gmail.com", type = "string", description = "User Login")
    private String login;

  //  @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "123456qW", type = "string", description = "User password")
    private String userPassword;
}
