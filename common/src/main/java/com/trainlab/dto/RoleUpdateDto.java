package com.trainlab.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RoleUpdateDto {
    @Size(max = 30)
    @NotNull
    @NotEmpty
    private String roleName;

    @NotNull
    @Column
    private Timestamp created = Timestamp.valueOf(LocalDateTime.now().withNano(0));

    @NotNull
    @Column
    private Timestamp changed = Timestamp.valueOf(LocalDateTime.now().withNano(0));

}
