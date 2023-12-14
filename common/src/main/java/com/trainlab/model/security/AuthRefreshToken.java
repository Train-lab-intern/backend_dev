package com.trainlab.model.security;

import com.trainlab.validation.ValidFingerprint;
import com.trainlab.validation.ValidRefreshToken;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
public class AuthRefreshToken {

    @NotBlank(message = "Refresh token is empty.")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "389f9a83-72b3-4697-af83-a1689d147af5",
            type = "String", description = "User refreshToken")
    @ValidRefreshToken
    String refreshToken;

    @NotBlank(message = "Fingerprint is empty.")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "2391f9a1c310f7e476682ee7c8c0213d3b06fd2d",
            type = "String", description = "Client fingerprint")
    @ValidFingerprint
    String fingerprint;
}
