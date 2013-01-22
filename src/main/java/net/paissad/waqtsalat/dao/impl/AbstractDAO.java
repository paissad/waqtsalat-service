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

package net.paissad.waqtsalat.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NamedQuery;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

import net.paissad.waqtsalat.conf.DBConf;
import net.paissad.waqtsalat.dao.DAO;
import net.paissad.waqtsalat.dao.DAOEntry;

/**
 * @param <T>
 * 
 * @author paissad
 */
abstract class AbstractDAO<T extends DAOEntry> implements DAO<T> {

    private static final long serialVersionUID = 1L;

    private EntityManager     entityManager;

    public AbstractDAO() {
        this.entityManager = this.getEntityManager();
    }

    @Override
    public void create(final T entry) {
        try {
            this.getEntityManager().getTransaction().begin();
            this.getEntityManager().persist(entry);
            this.getEntityManager().getTransaction().commit();
        } catch (final RuntimeException e) {
            this.wrapAndTreatException(this.getEntityManager(), e);
        }
    }

    @Override
    public void createAll(final Collection<T> entries) {
        try {
            this.getEntityManager().getTransaction().begin();
            for (final DAOEntry entry : entries) {
                this.getEntityManager().persist(entry);
            }
            this.getEntityManager().getTransaction().commit();
        } catch (final RuntimeException e) {
            this.wrapAndTreatException(this.getEntityManager(), e);
        }
    }

    @Override
    public T find(final Long id) {
        return this.getEntityManager().find(this.getEntityClass(), id);
    }

    @Override
    public Collection<T> findAll() {
        final CriteriaQuery<T> cq = this.getEntityManager().getCriteriaBuilder().createQuery(this.getEntityClass());
        cq.select(cq.from(this.getEntityClass()));
        return this.getEntityManager().createQuery(cq).getResultList();
    }

    @Override
    public void update(final T entry) {
        try {
            this.getEntityManager().getTransaction().begin();
            this.getEntityManager().merge(entry);
            this.getEntityManager().getTransaction().commit();
        } catch (final RuntimeException e) {
            this.wrapAndTreatException(this.getEntityManager(), e);
        }
    }

    @Override
    public void delete(final T entry) {
        try {
            this.getEntityManager().getTransaction().begin();
            this.getEntityManager().remove(entry);
            this.getEntityManager().getTransaction().commit();
        } catch (final RuntimeException e) {
            this.wrapAndTreatException(this.getEntityManager(), e);
        }
    }

    /**
     * Retrieves the object/entity/model using the key/value pair.
     * 
     * @param fieldName - The name of the key/field.
     * @param value - The value for the specified key/field.
     * @return The entity/object or <code>null</code> if not found.
     */
    @SuppressWarnings("unchecked")
    protected T findEntityHavingValue(final String fieldName, final Object value) {
        final String jpql = "SELECT x FROM " + this.getEntityClass().getName() + " x WHERE x." + fieldName + " = :"
                + fieldName;
        final Query q = this.getEntityManager().createQuery(jpql, this.getEntityClass());
        q.setParameter(fieldName, value);
        T result = null;
        try {
            result = (T) q.getSingleResult();
        } catch (final NoResultException nre) {
            // No result found !
        }
        return result;
    }

    /**
     * Retrieves the list of all entities/objects having the specified value for the specified field.
     * 
     * @param fieldName - The name of the field.
     * @param value - The value for the specified field.
     * @return The list of objects found or an empty list.
     */
    protected Collection<T> findAllEntitiesHavingValue(final String fieldName, final Object value) {
        final String jpql = "SELECT x FROM " + this.getEntityClass().getName() + " x WHERE x." + fieldName + " = :"
                + fieldName;
        final Query q = this.getEntityManager().createQuery(jpql, this.getEntityClass());
        q.setParameter(fieldName, value);
        @SuppressWarnings("unchecked")
        final List<T> result = q.getResultList();
        return result;
    }

    /**
     * Retrieves the specified entity/object using the specified {@link NamedQuery} having the specified name.
     * 
     * @param queryName - The name of the {@link NamedQuery} to use.
     * @param parameters - The list of parameters to pass to the {@link NamedQuery}.
     * @return The entity/object or <tt>null</tt> if not found.
     */
    @SuppressWarnings("unchecked")
    protected T findEntityByUsingQuery(final String queryName, final Map<String, Object> parameters) {
        final Query q = this.getEntityManager().createNamedQuery(queryName);
        if (parameters != null) {
            final Iterator<Entry<String, Object>> iter = parameters.entrySet().iterator();
            while (iter.hasNext()) {
                final Entry<String, Object> o = iter.next();
                q.setParameter(o.getKey(), o.getValue());
            }
        }
        T result = null;
        try {
            result = (T) q.getSingleResult();
        } catch (final NoResultException nre) {
            // no result
        }
        return result;
    }

    /**
     * Retrieves the list of entities/object using the specified {@link NamedQuery} having the specified name.
     * 
     * @param queryName - The name of the {@link NamedQuery} to use.
     * @param parameters - The list of parameters to pass to the {@link NamedQuery}.
     * @return The list of entities/objects or an empty list.
     */
    protected Collection<T> findAllEntitiesByUsingQuery(final String queryName, final Map<String, Object> parameters) {
        final Query q = this.getEntityManager().createNamedQuery(queryName);
        if (parameters != null) {
            final Iterator<Entry<String, Object>> iter = parameters.entrySet().iterator();
            while (iter.hasNext()) {
                final Entry<String, Object> o = iter.next();
                q.setParameter(o.getKey(), o.getValue());
            }
        }
        @SuppressWarnings("unchecked")
        final List<T> result = q.getResultList();
        return result;
    }

    /**
     * @return The type of the class of the entity to work with.
     */
    protected Class<T> getEntityClass() {
        final ParameterizedType ptype = (ParameterizedType) this.getClass().getGenericSuperclass();
        @SuppressWarnings({ "unchecked" })
        final Class<T> result = (Class<T>) ptype.getActualTypeArguments()[0];
        return result;
    }

    protected EntityManager getEntityManager() {
        if (this.entityManager == null) {
            this.entityManager = DBConf.getEntityManager();
        }
        return this.entityManager;
    }

    /**
     * <p>
     * This method is a wrapper which should be invoked when a exception occurs during a transaction.
     * </p>
     * <p>
     * Here are the tasks which are performed in the specified order:
     * <ol>
     * <li>Retrieve the transaction from the specified entity manager.</li>
     * <li>Rollback the transaction if it is active.</li>
     * <li>Close the entity manager.</li>
     * <li>Re-throw the specified exception which has been the cause.</li>
     * </ol>
     * </p>
     * 
     * @param em - The {@link EntityManager}.
     * @param e - The exception to treat & re-throw.
     */
    @SuppressWarnings("finally")
    // An exception is thrown in the finally block
    protected void wrapAndTreatException(final EntityManager em, final RuntimeException e) {
        try {
            final EntityTransaction tr = em.getTransaction();
            if (tr.isActive()) {
                tr.rollback();
            }
        } finally {
            try {
                em.close();
            } catch (IllegalStateException ise) {
                // Should never happen though, since the entity manager is not container managed.
            }
            throw e;
        }
    }
}
