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

package net.paissad.waqtsalat.logging;

import static net.paissad.waqtsalat.WSConstants.WS_DEFAULT_MAX_LOG_SIZE;
import ch.qos.logback.core.PropertyDefinerBase;

/**
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
public class RollingFileSizeDefiner extends PropertyDefinerBase {

    private static String maxSize = WS_DEFAULT_MAX_LOG_SIZE;

    /*
     * (non-Javadoc)
     * 
     * @see ch.qos.logback.core.spi.PropertyDefiner#getPropertyValue()
     */
    @Override
    public String getPropertyValue() {
        return maxSize;
    }

    /**
     * @param maxSize - The maximum size of the log file before
     *            rolling.<b>Example</b>: 5MB
     * @see LogReloader#reload()
     */
    public static void setMaxSize(String maxSize) {
        RollingFileSizeDefiner.maxSize = maxSize;
        LogReloader.reload();
    }
}
