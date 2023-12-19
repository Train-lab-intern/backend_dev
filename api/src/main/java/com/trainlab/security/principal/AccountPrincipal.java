package com.trainlab.security.principal;

import com.trainlab.model.Role;

import java.util.List;

public interface AccountPrincipal {

    Long getId();

    List<Role> getRole();
}
