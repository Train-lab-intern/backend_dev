package com.trainlab.service;

import com.trainlab.dto.request.RoleRequest;
import com.trainlab.model.Role;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RoleService {


    @Transactional
    Role create(RoleRequest roleRequest);

    Role receiveById(Integer id);

    Role receiveByRoleName(String roleName);

    List<Role> findAll();

    @Transactional
    Role update(RoleRequest roleRequest, Integer id);
}
