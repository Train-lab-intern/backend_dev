package com.trainlab.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Schema(description = "User Response")
public class UserDto {
    private Long id;

    private String username;

    private String email;

    private boolean active;

    private Timestamp created;
}
