package com.trainlab.security.dto;

import com.trainlab.dto.UserDto;
import com.trainlab.security.security.AccessToken;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Value
@Builder
public class AuthResponseDto {

    @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c", type = "string", description = "Authentication token")
    AccessToken token;

    @Schema
    UserDto userDto;
}