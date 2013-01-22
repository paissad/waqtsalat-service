package net.paissad.waqtsalat.dao.impl;

import net.paissad.waqtsalat.domain.Role;
import net.paissad.waqtsalat.enums.RoleType;

public class RoleDAO extends AbstractDAO<Role> {

    private static final long serialVersionUID = 1L;

    public Role findByType(final RoleType type) {
        return this.findEntityHavingValue("type", type);
    }

}
