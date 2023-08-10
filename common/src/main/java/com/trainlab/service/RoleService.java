package com.trainlab.service;

import com.trainlab.dto.RoleRequestDto;
import com.trainlab.model.Role;

import java.util.List;

public interface RoleService {

    Role create(RoleRequestDto roleRequestDto);

    Role findById(Integer id);

    Role findByRoleName(String roleName);

    List<Role> findAll();

    Role update(RoleRequestDto roleRequestDto, Integer id);
}
