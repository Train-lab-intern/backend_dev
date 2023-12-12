package com.trainlab.service;

import com.trainlab.dto.RoleCreateDto;
import com.trainlab.dto.RoleDto;
import com.trainlab.dto.RoleUpdateDto;

import java.util.List;

public interface RoleService {

    RoleDto create(RoleCreateDto roleCreateDto);

    RoleDto findById(Integer id);

    RoleDto findByRoleName(String roleName);

    List<RoleDto> findAll();

    RoleDto update(RoleUpdateDto roleUpdateDto, Integer id);

    void delete(Integer id);
}
