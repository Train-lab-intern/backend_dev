package com.trainlab.security.principal;

import com.trainlab.dto.RoleDto;

import java.util.List;

public interface AccountPrincipal {

    Long getId();

    List<RoleDto> getRole();
}
