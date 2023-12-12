package com.trainlab.security.principal;

import com.trainlab.model.Role;
import lombok.Value;

import java.util.List;

@Value
public class UserPrincipal implements AccountPrincipal {

    Long id;
    List<Role> roles;

    public List<Role> getRole() {
        return roles;
    }
}
