package com.trainlab.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users", schema = "public")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "generated_name", unique = true, nullable = false)
    private String generatedName;

    @Column(name = "username")
    private String username;

    @Column(name = "surname")
    private String surname;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "email", column = @Column(name = "email", nullable = false, length = 256)),
            @AttributeOverride(name = "userPassword", column = @Column(name = "user_password", nullable = false, length = 256))
    })
    private AuthenticationInfo authenticationInfo;

    @NotNull
    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = false;

    @NotNull
    @Column
    private Timestamp created;

    @NotNull
    @Column
    private Timestamp changed;

    @NotNull
    @Column(name = "is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private List<Role> roles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "speciality_id")
    private Speciality speciality;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level_id")
    private UserLevel userLevel;
}
