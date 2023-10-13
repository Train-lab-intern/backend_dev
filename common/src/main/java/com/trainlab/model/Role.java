package com.trainlab.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "roles", schema = "public")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Size(max = 256)
    @NotNull
    @NotEmpty
    @Column(name = "role_name", unique = true, nullable = false, length = 256)
    private String roleName;

    @NotNull
    @Column
    private Timestamp created = Timestamp.valueOf(LocalDateTime.now().withNano(0));

    @NotNull
    @Column
    private Timestamp changed = Timestamp.valueOf(LocalDateTime.now().withNano(0));

    @NotNull
    @Column
    @Builder.Default
    private Boolean isDeleted = false;
}