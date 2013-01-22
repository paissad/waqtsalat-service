package net.paissad.waqtsalat.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.Email;

import net.paissad.waqtsalat.dao.DAOEntry;

@Getter
@Setter
@Entity
@Table(name = "ws_accounts")
@NamedQueries({ @NamedQuery(name = "Account.findByAuth", query = "SELECT a FROM Account a WHERE a.auth = :auth") })
public class Account implements DAOEntry {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long              id;

    @Email
    @Column(unique = true, nullable = false, length = 256, columnDefinition = "varchar(256)")
    private String            email;

    @Pattern(regexp = "^[\\p{Alnum}\\-_\\.]{3,30}$")
    @Column(unique = true, nullable = false, length = 30)
    private String            username;

    @Size(min = 8, max = 128)
    @Column(nullable = false, length = 128)
    private String            password;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date              registrationDate;

    @NotNull
    @OneToOne(optional = false, cascade = CascadeType.ALL, orphanRemoval = true)
    private Auth              auth;

    @Column(unique = true)
    private String            activationKey;

    @ManyToMany
    @JoinTable(name = "ws_accounts_roles", joinColumns = @JoinColumn(name = "account_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role>         roles;

    @OneToOne(optional = true, cascade = CascadeType.ALL, orphanRemoval = true)
    private AccountMetadata   metadata;

    public Account() {
        this.roles = new HashSet<Role>();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.email == null) ? 0 : this.email.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Account)) return false;
        final Account other = (Account) obj;
        if (this.email == null) {
            if (other.email != null) return false;
        } else if (!this.email.equals(other.email)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "Account [email=" + this.email + ", username=" + this.username + ", registrationDate="
                + this.registrationDate + "]";
    }

}
