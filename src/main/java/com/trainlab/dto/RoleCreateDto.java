package com.trainlab.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Request role DTO data from UI to create role")
public class RoleCreateDto {
    @Size(max = 30)
    @NotNull
    @NotEmpty
    private String roleName;

}
