package com.trainlab.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Schema(description = "Request role DTO data from UI to update role")
public class RoleUpdateDto {
    private Integer id;

    @Size(max = 30)
    @NotNull
    private String roleName;

    @Builder.Default
    private boolean isDeleted = false;

}
