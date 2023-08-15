package com.trainlab.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Schema(description = "Role DTO data to UI role response")
public class RoleDto {
    private Integer id;

    private String roleName;

    private Timestamp created;

    private Timestamp changed;

}
