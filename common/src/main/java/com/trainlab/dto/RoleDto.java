package com.trainlab.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
@Schema(description = "Role response")
public class RoleDto {
    private Integer id;

    private String roleName;

    private Timestamp created;

    private Timestamp changed;

}
