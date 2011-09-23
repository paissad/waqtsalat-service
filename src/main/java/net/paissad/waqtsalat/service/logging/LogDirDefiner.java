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

        String sysTempDirName = System.getProperty("java.io.tmpdir");
        String homeDir = System.getProperty("user.home");

        try {
            // Try to use the specified log directory.
            if (logDir != null) {
                if (!logDir.exists())
                    logDir.mkdirs();
                if (logDir.isDirectory())
                    return logDir.getAbsolutePath();
                // If logDir is a file, then try further approaches !
            }

            // Otherwise, try to use a specified-user directory into system
            // home directory.
            File dir = new File(homeDir, ".jcamstream" + File.separator + "logs");
            dir.mkdirs();
            if (dir.isDirectory() && dir.canWrite()) {
                logDir = dir;
                return dir.getAbsolutePath();
            }

        } catch (Exception e) {
            // Do nothing !
        }

        // If previous tries failed, then use the system temporary directory !
        logDir = new File(sysTempDirName);
        return sysTempDirName;
    }

    /**
     * Sets the directory where to place the log file.
     * <p>
     * <b>Note</b>: Do not forget to reload the logger after the use of this
     * function in order to make changes effective.
     * </p>
     * 
     * @param logDir
     *            - The directory where to place the log file.
     * 
     * @see LogReloader#reload()
     */
    public static void setLogDir(File logDir) {
        LogDirDefiner.logDir = logDir;
    }

    public static File getCurrentLogDir() {
        return logDir;
    }
}
