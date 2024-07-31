package com.trainlab.security.principal;

import com.trainlab.dto.RoleDto;
import com.trainlab.model.Role;

import java.util.List;

public interface AccountPrincipal {

    Long getId();

    List<RoleDto> getRole();
}
