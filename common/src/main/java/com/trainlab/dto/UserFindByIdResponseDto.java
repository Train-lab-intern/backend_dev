package com.trainlab.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "User FindById Response")
public class UserFindByIdResponseDto {
    private String username;

    private String email;

    private boolean active;
}
