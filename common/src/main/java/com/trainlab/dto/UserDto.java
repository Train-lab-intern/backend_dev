package com.trainlab.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
@Schema(description = "User Response")
public class UserDto {
    private Long id;

    private String username;

    private String email;

    private Timestamp created;

    private Timestamp changed;

    private boolean active;
}
