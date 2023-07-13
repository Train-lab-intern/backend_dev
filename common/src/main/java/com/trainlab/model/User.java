package com.trainlab.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.cache.annotation.Cacheable;

import java.sql.Timestamp;
import java.util.Set;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Cacheable("users")
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "username", nullable = false)
    private String username;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "email", column = @Column(name = "email", nullable = false, length = 256)),
            @AttributeOverride(name = "userPassword", column = @Column(name = "user_password", nullable = false, length = 256))
    })
    private AuthenticationInfo authenticationInfo;

    @NotNull
    @Column
    @JsonIgnore
    private Timestamp created;

    @NotNull
    @Column
    @JsonIgnore
    private Timestamp changed;

    @NotNull
    @Column(name = "is_deleted")
    @JsonIgnore
    private boolean isDeleted = false;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles;
}
