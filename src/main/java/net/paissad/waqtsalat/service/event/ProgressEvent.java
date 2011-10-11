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

package net.paissad.waqtsalat.service.event;

/**
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
public interface ProgressEvent {

    /**
     * @return The number of bytes already transferred
     */
    public long getProgress();

    /**
     * @return The total number of bytes, or a value lower or equal to zero if
     *         not known. <b>Note</b>: This value is normally set only once and
     *         is not intended to change over the time. But you are free to
     *         implement it as you want.
     */
    public long getTotal();
}
