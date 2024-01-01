package com.trainlab.security.principal;

import com.trainlab.dto.RoleDto;
import com.trainlab.model.Role;
import lombok.Value;

import java.util.List;

@Value
public class UserPrincipal implements AccountPrincipal {

    Long id;
    List<RoleDto> roles;

    public List<RoleDto> getRole() {
        return roles;
    }
}
