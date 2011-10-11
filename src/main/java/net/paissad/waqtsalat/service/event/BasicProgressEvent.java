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
public class BasicProgressEvent implements ProgressEvent {

    private final long total;
    private long       progress;

    public BasicProgressEvent(final long total, final long progress) {
        this.total = total;
        this.progress = progress;
    }

    @Override
    public synchronized long getProgress() {
        return this.progress;
    }

    /**
     * @param progress - The new value of progression (transferred bytes)
     */
    public synchronized void setProgress(final long progress) {
        this.progress = progress;
    }

    @Override
    public synchronized long getTotal() {
        return this.total;
    }

    /**
     * Adds the specified amount of bytes to the current transferred bytes.
     * 
     * @param amount - amount of bytes to add to the current progression value.
     */
    public synchronized void add(final long amount) {
        setProgress(getProgress() + amount);
    }

}
