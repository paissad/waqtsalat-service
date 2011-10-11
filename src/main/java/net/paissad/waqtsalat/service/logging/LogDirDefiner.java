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

package net.paissad.waqtsalat.service.logging;

import java.io.File;

import ch.qos.logback.core.PropertyDefinerBase;

import net.paissad.waqtsalat.service.WSConstants;

/**
 * This class defines the directory where to place the log file.
 * 
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
public class LogDirDefiner extends PropertyDefinerBase {

    private static File logDir = null;

    /*
     * (non-Javadoc)
     * 
     * @see ch.qos.logback.core.spi.PropertyDefiner#getPropertyValue()
     */
    @Override
    public String getPropertyValue() {
        try {
            if (logDir != null) {
                if (!logDir.exists()) {
                    logDir.mkdirs();
                }
                if (logDir.isDirectory()) {
                    return logDir.getAbsolutePath();
                }
            }
            WSConstants.WS_DEFAULT_LOG_DIR.mkdirs();
            if (WSConstants.WS_DEFAULT_LOG_DIR.exists()) {
                return WSConstants.WS_DEFAULT_LOG_DIR.getAbsolutePath();
            }
        } catch (Exception e) {
        }
        return System.getProperty("java.io.tmpdir");
    }

    /**
     * Sets the directory where to place the log file.
     * <p>
     * <b>NOTE</b>: Do not forget to reload the logger after the use of this
     * function in order to make changes effective.
     * </p>
     * 
     * @param logDir - The directory where to place the log file.
     * 
     * @see LogReloader#reload()
     */
    public static void setLogDir(final File logDir) {
        LogDirDefiner.logDir = logDir;
    }

    public static File getCurrentLogDir() {
        return logDir;
    }
}
