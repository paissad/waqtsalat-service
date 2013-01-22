package net.paissad.waqtsalat.dao.impl;

import java.util.HashMap;
import java.util.Map;

import net.paissad.waqtsalat.domain.Account;
import net.paissad.waqtsalat.domain.Auth;

public class AccountDAO extends AbstractDAO<Account> {

    private static final long serialVersionUID = 1L;

    public Account findByName(final String name) {
        return this.findEntityHavingValue("username", name);
    }

    public Account findByEmail(final String email) {
        return this.findEntityHavingValue("email", email);
    }

    public Account findByActivationKey(final String activationKey) {
        return this.findEntityHavingValue("activationKey", activationKey);
    }

    public Account findByAuth(final Auth auth) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("auth", auth);
        return this.findEntityByUsingQuery("Account.findByAuth", parameters);
    }

}
