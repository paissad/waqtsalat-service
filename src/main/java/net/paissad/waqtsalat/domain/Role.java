package net.paissad.waqtsalat.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import net.paissad.waqtsalat.dao.DAOEntry;
import net.paissad.waqtsalat.enums.RoleType;

@Getter
@Setter
@Entity
@Table(name = "ws_roles")
public class Role implements DAOEntry {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long              id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private RoleType          type;

    @ManyToMany(mappedBy = "roles")
    private Set<Account>      accounts;

    public Role() {
        this(null);
    }

    public Role(final RoleType type) {
        this.accounts = new HashSet<Account>();
        this.type = type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Role)) return false;
        Role other = (Role) obj;
        if (type != other.type) return false;
        return true;
    }

    @Override
    public String toString() {
        return "Role [type=" + type + "]";
    }

}
