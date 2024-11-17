package com.trainlab.model.security;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
public class AuthRefreshToken {

    @NotBlank(message = "Refresh token is empty")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "389f9a83-72b3-4697-af83-a1689d147af5",
            type = "String", description = "User refresh token")
    @Pattern(regexp = "[\\d\\-a-f]{36}", message = "Incorrect refresh token")
    String refreshToken;

}
