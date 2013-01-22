/*
 * WaqtSalat, for indicating the muslim prayers times in most cities. Copyright
 * (C) 2011 Papa Issa DIAKHATE (paissad).
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * PLEASE DO NOT REMOVE THIS COPYRIGHT BLOCK.
 */

package net.paissad.waqtsalat.factory;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.paissad.waqtsalat.util.JdbcUtils;

/**
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
public class DBConnection {

    private static final String PROTOCOL = "jdbc:h2:";
    private static final String DBNAME   = "worldcitiespop";
    private static final String SETTINGS = ";LARGE_TRANSACTIONS=true;OPTIMIZE_IN_SELECT=true;OPTIMIZE_OR=true";
    private static final String BASEDIR  = "extras/geoip/db";

    private static Logger       logger   = LoggerFactory.getLogger(DBConnection.class);

    static {
        System.setProperty("h2.baseDir", new File(BASEDIR).getAbsolutePath());
    }

    private DBConnection() {
    }

    /**
     * @return An instance of database connection.
     * @throws SQLException
     */
    public static Connection getInstance() throws SQLException {
        try {
            return DriverManager.getConnection(getConnectionURL(), "", "");

        } catch (SQLException sqle) {
            String errMsg = "Error while getting an instance of database connection.\n";
            logger.error(errMsg, sqle);
            JdbcUtils.printSQLException(sqle);
            throw new SQLException(errMsg, sqle);
        }
    }

    private static String getConnectionURL() {
        return PROTOCOL + DBNAME + SETTINGS;
    }

}
