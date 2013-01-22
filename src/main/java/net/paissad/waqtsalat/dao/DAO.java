/*
 * This file is part of WaqtSalat-Service.
 * 
 * WaqtSalat-Service is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * WaqtSalat-Service is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General License for more details.
 * 
 * You should have received a copy of the GNU General License
 * along with WaqtSalat-Service. If not, see <http://www.gnu.org/licenses/>.
 */

package net.paissad.waqtsalat.dao;

import java.io.Serializable;
import java.util.Collection;

/**
 * 
 * The top interface of all DAO objects.
 * 
 * @param <T> - The type of the entity which is a subclass of {@link DAOEntry}.
 * 
 * @author paissad
 */
public interface DAO<T extends DAOEntry> extends Serializable {
    /**
     * @param entry - The entity/object to create/persist.
     */
    void create(T entry);

    /**
     * Persists a list of entities/objects.
     * 
     * @param entries - The list of entities to create/persist.
     */
    void createAll(Collection<T> entries);

    /**
     * 
     * @param id - The id of the entity/object to look for.
     * @return The entity which has been found or <tt>null</tt> if not found.
     */
    T find(Long id);

    /**
     * @return The list of all persisted entities/objects.
     */
    Collection<T> findAll();

    /**
     * @param entry - The entity/object/model that must be updated.
     */
    void update(T entry);

    /**
     * @param entry - The entity/object/model that must be deleted.
     */
    void delete(T entry);
}
