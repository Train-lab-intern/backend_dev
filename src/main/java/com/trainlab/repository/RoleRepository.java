package com.trainlab.repository;

import com.trainlab.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(String roleName);

    boolean existsByRoleName(String roleName);

}
