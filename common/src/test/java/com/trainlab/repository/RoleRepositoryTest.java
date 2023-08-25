package com.trainlab.repository;

import com.trainlab.TestApplication;
import com.trainlab.model.Role;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@SpringJUnitConfig(TestApplication.class)
@TestPropertySource(properties = {"spring.profiles.active=test"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    @BeforeAll
    public void insertRoles() {
        Role role1 = Role.builder()
                .id(1)
                .roleName("ROLE_USER")
                .created(Timestamp.valueOf("2023-01-01 00:00:00"))
                .changed(Timestamp.valueOf("2023-01-02 00:00:00"))
                .isDeleted(false)
                .build();
        roleRepository.saveAndFlush(role1);
        Role role2 = Role.builder()
                .id(2)
                .roleName("ROLE_ADMIN")
                .created(Timestamp.valueOf("2023-02-01 00:00:00"))
                .changed(Timestamp.valueOf("2023-02-02 00:00:00"))
                .isDeleted(false)
                .build();
        roleRepository.saveAndFlush(role2);
    }

    @AfterAll
    void clearRoles() {
        roleRepository.deleteAll();
    }

    @Test
    void findByRoleName() {
        String roleName = "ROLE_USER";
        Role expected = Role.builder()
                .id(1)
                .roleName("ROLE_USER")
                .created(Timestamp.valueOf("2023-01-01 00:00:00"))
                .changed(Timestamp.valueOf("2023-01-02 00:00:00"))
                .isDeleted(false)
                .build();
        Optional<Role> actual = roleRepository.findByRoleName(roleName);
        assertEquals(expected, actual.orElse(null));
    }

    @Test
    void existsByRoleName_should_return_true() {
        String roleName = "ROLE_USER";
        boolean actual = roleRepository.existsByRoleName(roleName);
        assertTrue(actual);
    }

    @Test
    void existsByRoleName_should_return_false() {
        String roleName = "ROLE_GUEST";
        boolean actual = roleRepository.existsByRoleName(roleName);
        assertFalse(actual);
    }
}