package com.trainlab.service;

import com.trainlab.dto.RoleCreateDto;
import com.trainlab.dto.RoleDto;
import com.trainlab.model.Role;

import java.util.List;

public interface RoleService {

    RoleDto create(RoleCreateDto roleCreateDto);

    RoleDto findById(Integer id);

    RoleDto findByRoleName(String roleName);

    List<RoleDto> findAll();

    RoleDto update(RoleDto roleDto, Integer id);
}
