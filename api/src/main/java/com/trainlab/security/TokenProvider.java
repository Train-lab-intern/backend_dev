package com.trainlab.security;

import com.trainlab.security.model.AccessToken;
import com.trainlab.model.security.RefreshToken;
import com.trainlab.security.principal.AccountPrincipal;

public interface TokenProvider {

    AccessToken generate(AccountPrincipal accountPrincipal);

    AccountPrincipal authenticate(String tokenValue);

    RefreshToken generateRefreshToken();
}
