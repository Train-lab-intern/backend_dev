package com.trainlab.security;

import com.trainlab.security.security.AccessToken;
import com.trainlab.security.principal.AccountPrincipal;

public interface TokenProvider {

    AccessToken generate(AccountPrincipal accountPrincipal);

    AccountPrincipal authenticate(String tokenValue);
}
