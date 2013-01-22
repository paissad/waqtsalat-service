package net.paissad.waqtsalat.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import net.paissad.waqtsalat.dao.DAOEntry;

@Getter
@Setter
@Entity
@Table(name = "ws_auths")
@NamedQueries({ @NamedQuery(name = "Auth.findByAccount", query = "SELECT a FROM Auth a WHERE a.account = :account") })
public class Auth implements DAOEntry {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long              id;

    @NotNull
    @Column(unique = true, nullable = false, length = 128)
    private String            value;

    @OneToOne(optional = false, mappedBy = "auth")
    private Account           account;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Auth)) return false;
        Auth other = (Auth) obj;
        if (value == null) {
            if (other.value != null) return false;
        } else if (!value.equals(other.value)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "Auth [value=" + value + ", account=" + account + "]";
    }

}
