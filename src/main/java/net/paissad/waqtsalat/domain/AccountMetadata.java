package net.paissad.waqtsalat.domain;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

import net.paissad.waqtsalat.dao.DAOEntry;

@Getter
@Setter
@Entity
@Table(name = "ws_accounts_metadatas")
public class AccountMetadata implements DAOEntry {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long              id;

    @OneToOne(optional = false, mappedBy = "metadata")
    private Account           account;

    @Column(unique = true, length = 64)
    private String            passwordResetKey;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar          passwordResetDeadline;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.account == null) ? 0 : this.account.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof AccountMetadata)) return false;
        AccountMetadata other = (AccountMetadata) obj;
        if (this.account == null) {
            if (other.account != null) return false;
        } else if (!this.account.equals(other.account)) return false;
        return true;
    }

}
