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

package net.paissad.waqtsalat.service.dao;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import net.paissad.waqtsalat.service.bean.User;
import net.paissad.waqtsalat.service.factory.EntityManagerFactoryProxy;

/**
 * @author paissad
 * 
 */
public class UserDAO extends AbstractDAO<User> implements DAO<User> {

    private static final long serialVersionUID = 1L;

    @Override
    public User create(User user) {
        EntityManager em = EntityManagerFactoryProxy.getInstance();
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            return null; // XXX
        } finally {
            em.close();
        }
    }

    @Override
    public User delete(long id) {
        EntityManager em = EntityManagerFactoryProxy.getInstance();
        try {
            Query q = em.createQuery("SELECT u from ws_users u WHERE id = " + id);
            User u = (User) q.getSingleResult();
            em.remove(u);
            return u;
        } finally {
            em.close();
        }
    }

    @Override
    public User find(long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public User update(User user) {
        // TODO Auto-generated method stub
        return null;
    }

}
