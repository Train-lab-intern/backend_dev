package com.trainlab.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RoleDto {
    @Size(max = 30)
    @NotNull
    private String roleName;

    @NotNull
    private Timestamp created;

    @NotNull
    private Timestamp changed;

}
