/*
 * This file is part of WaqtSalat-Service.
 * 
 * WaqtSalat-Service is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * WaqtSalat-Service is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with WaqtSalat-Service. If not, see <http://www.gnu.org/licenses/>.
 */

package net.paissad.waqtsalat.service.bean;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import net.paissad.waqtsalat.service.I18N;
import net.paissad.waqtsalat.service.dao.UserDAO;

/**
 * @author paissad
 * 
 */
@ManagedBean(name = "user")
@SessionScoped
@Entity
@Table(name = "ws_users", uniqueConstraints = @UniqueConstraint(columnNames = { "NICKNAME", "EMAIL" }))
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @SuppressWarnings("unused")
    private int               id;
    private String            nickName;
    private String            email;
    private String            password;

    @Transient
    private UserDAO           userDAO;

    public User() {
        this(null, null);
    }

    public User(final String nickName, final String email) {
        this.nickName = nickName;
        this.email = email;
        this.userDAO = new UserDAO();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void register() {
        this.userDAO.create(this);
        FacesContext.getCurrentInstance().addMessage("register",
                new FacesMessage(I18N.getString("Registered_successfuly")));
    }

    @Override
    public String toString() {
        return this.getNickName() + " - " + this.getEmail();
    }
}
