package com.trainlab.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Table(name = "frontend_data")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FrontendData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "front_id", nullable = false)
    private Float frontId;

    @NotNull
    @Column(name = "text", nullable = false)
    private String test;

    @JsonIgnore
    @NotNull
    @Column(name = "created", nullable = false)
    private Timestamp created;

    @JsonIgnore
    @NotNull
    @Column(name = "changed", nullable = false)
    private Timestamp changed;

    @JsonIgnore
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;
}
