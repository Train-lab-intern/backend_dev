package com.trainlab.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "speciality", schema = "public")
public class Speciality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "speciality_id")
    private int id;

    @Column(name = "speciality_name", unique = true, nullable = false)
    private String specialityName;
}
