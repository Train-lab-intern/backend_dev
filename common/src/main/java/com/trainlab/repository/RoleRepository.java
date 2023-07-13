package com.trainlab.repository;

import com.trainlab.model.Role;
import jakarta.validation.constraints.NotNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Cacheable("c_roles")
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @NotNull
    List<Role> findAll();
}
