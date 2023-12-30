package com.trainlab.model;

import com.trainlab.enums.Speciality;
import com.trainlab.enums.UserLevel;
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

    @Column(name = "username", nullable = false, unique = true)
    private String username;

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

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "secondName")
    private  String secondName;

    @Column(name = "userLevel")
    @Enumerated(EnumType.STRING)
    private UserLevel userLevel;

    @Column(name = "speciality")
    private Speciality speciality;

}
