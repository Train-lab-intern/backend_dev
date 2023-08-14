package com.trainlab.service.impl;

import com.trainlab.dto.RoleCreateDto;
import com.trainlab.dto.RoleDto;
import com.trainlab.dto.RoleUpdateDto;
import com.trainlab.exception.ObjectIsExistException;
import com.trainlab.mapper.RoleMapper;
import com.trainlab.model.Role;
import com.trainlab.repository.RoleRepository;
import com.trainlab.service.RoleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public List<RoleDto> findAll() {
        List<Role> roles = roleRepository.findAll();

        if (roles.isEmpty()) {
            throw new EntityNotFoundException("Roles not found");
        }

        return roles.stream()
                .map(roleMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public RoleDto create(RoleCreateDto roleCreateDto) {
        if (roleRepository.existsByRoleName(roleCreateDto.getRoleName()))
            throw new ObjectIsExistException("The same role already exists");
        Role role = roleRepository.saveAndFlush(roleMapper.toEntity(roleCreateDto));
        return roleMapper.toDto(role);
    }

    @Override
    public RoleDto findById(Integer id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Role not found"));
        return roleMapper.toDto(role);
    }

    @Override
    public RoleDto findByRoleName(String roleName) {
        Role role = roleRepository.findByRoleName(roleName).orElseThrow(() -> new EntityNotFoundException("Role not found"));
        return roleMapper.toDto(role);
    }

    @Override
    public RoleDto update(RoleUpdateDto roleUpdateDto, Integer id) {
        Optional<Role> role = roleRepository.findById(id);

        if (roleRepository.existsByRoleName(roleUpdateDto.getRoleName()))
            throw new ObjectIsExistException("The same role already exists");

        Role updated = roleMapper.partialUpdateToEntity(roleUpdateDto, role.orElseThrow(() -> new EntityNotFoundException("Role not found")));
        Role roleSaved = roleRepository.saveAndFlush(updated);
        return roleMapper.toDto(roleSaved);
    }

}
