package net.paissad.waqtsalat.dao.impl;

import java.util.HashMap;
import java.util.Map;

import net.paissad.waqtsalat.domain.Account;
import net.paissad.waqtsalat.domain.Auth;

public class AuthDAO extends AbstractDAO<Auth> {

    private static final long serialVersionUID = 1L;

    public Auth findByValue(final String value) {
        return this.findEntityHavingValue("value", value);
    }

    public Auth findByAccount(final Account account) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("account", account);
        return this.findEntityByUsingQuery("Auth.findByAccount", parameters);
    }
}
