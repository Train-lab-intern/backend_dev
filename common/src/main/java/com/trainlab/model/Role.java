package com.trainlab.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
//@Cacheable("roles")
@Table(name = "roles", schema = "public")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Size(max = 256)
    @NotNull
    @Column(name = "role_name", nullable = false, length = 256)
    private String roleName;

    @NotNull
    @Column
    @JsonIgnore
    private Timestamp created;

    @NotNull
    @Column
    @JsonIgnore
    private Timestamp changed;

    @NotNull
    @Column
    @JsonIgnore
    private boolean isDeleted;
}