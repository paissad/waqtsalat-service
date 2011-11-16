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

import java.io.Serializable;

/**
 * @author paissad
 * @param <T>
 * 
 */
public interface DAO<T> extends Serializable {

    /**
     * 
     * @param obj - The new object to create (to persist).
     * @return The created object
     * @throws Exception
     */
    public T create(T obj) throws Exception;

    /**
     * 
     * @param id - The id of the object to delete
     * @return The deleted object.
     * @throws Exception
     */
    public T delete(long id) throws Exception;

    /**
     * 
     * @param id - The id of the object to search.
     * @return The object found or <code>null</code> if nothing is found.
     * @throws Exception
     */
    public T find(long id) throws Exception;

    /**
     * 
     * @param obj - The object to update.
     * @return The updated object.
     * @throws Exception
     */
    public T update(T obj) throws Exception;
}
