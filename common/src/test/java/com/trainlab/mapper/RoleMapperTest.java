package com.trainlab.mapper;

import com.trainlab.TestApplication;
import com.trainlab.dto.RoleCreateDto;
import com.trainlab.dto.RoleDto;
import com.trainlab.dto.RoleUpdateDto;
import com.trainlab.model.Role;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {TestApplication.class})
@RunWith(SpringRunner.class)
class RoleMapperTest {
    @Autowired
    private RoleMapper roleMapper;

    @Test
    void toEntity() {
        String name = "testName";
        RoleCreateDto roleCreateDto = RoleCreateDto.builder()
                .roleName(name)
                .build();

        Role expected = Role.builder()
                .roleName(name)
                .created(Timestamp.valueOf(LocalDateTime.now().withNano(0)))
                .changed(Timestamp.valueOf(LocalDateTime.now().withNano(0)))
                .isDeleted(false)
                .build();

        Role actual = roleMapper.toEntity(roleCreateDto);
        assertEquals(expected, actual);
    }

    @Test
    void partialUpdateToEntity() {
        Integer id = 1;
        String name = "testName";
        RoleUpdateDto roleUpdateDto = RoleUpdateDto.builder()
                .id(id)
                .roleName(name)
                .isDeleted(false)
                .build();
        Role role = Role.builder()
                .id(id)
                .roleName(name)
                .created(Timestamp.valueOf("2023-01-01 00:00:00"))
                .isDeleted(false)
                .build();

        Role expected = Role.builder()
                .id(id)
                .roleName(name)
                .created(Timestamp.valueOf("2023-01-01 00:00:00"))
                .changed(Timestamp.valueOf(LocalDateTime.now().withNano(0)))
                .isDeleted(false)
                .build();

        Role actual = roleMapper.partialUpdateToEntity(roleUpdateDto, role);
        assertEquals(expected, actual);
    }

    @Test
    void toDto() {
        Integer id = 1;
        String name = "testName";
        Role role = Role.builder()
                .id(id)
                .roleName(name)
                .created(Timestamp.valueOf("2023-01-01 00:00:00"))
                .changed(Timestamp.valueOf("2023-01-02 00:00:00"))
                .isDeleted(false)
                .build();

        RoleDto expected = RoleDto.builder()
                .id(id)
                .roleName(name)
                .created(Timestamp.valueOf("2023-01-01 00:00:00"))
                .changed(Timestamp.valueOf("2023-01-02 00:00:00"))
                .build();

        RoleDto actual = roleMapper.toDto(role);
        assertEquals(expected, actual);
    }
}