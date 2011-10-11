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

package net.paissad.waqtsalat.service.listener;

import net.paissad.waqtsalat.service.event.ProgressEvent;

/**
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
public interface ProgressListener {

    /**
     * Implement this API to receive progress events operations.
     * 
     * @param event - Everything to know about this event.
     */
    public void onProgress(ProgressEvent event);
}
