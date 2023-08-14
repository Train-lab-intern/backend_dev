package com.trainlab.controller;

import com.trainlab.dto.RoleCreateDto;
import com.trainlab.dto.RoleDto;
import com.trainlab.dto.RoleUpdateDto;
import com.trainlab.service.RoleService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/roles")
@RequiredArgsConstructor
public class RoleControllerImpl implements RoleController {

    private final RoleService roleService;

    @PostMapping("/")
    public ResponseEntity<RoleDto> createRole(@Valid @RequestBody @Parameter(description = "Role DTO", required = true) RoleCreateDto roleCreateDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.create(roleCreateDto));
    }

    @GetMapping
    public ResponseEntity<List<RoleDto>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> findRoleById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.findById(id));
    }

    @GetMapping("/")
    public ResponseEntity<RoleDto> findRoleByRoleName(@RequestBody String roleName) {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.findByRoleName(roleName));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDto> updateRole(@PathVariable("id") Integer id,
                                           @Valid @RequestBody RoleUpdateDto roleUpdateDto) {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.update(roleUpdateDto, id));
    }

}
