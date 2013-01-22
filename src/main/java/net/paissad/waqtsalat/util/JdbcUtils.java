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

package net.paissad.waqtsalat.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
public class JdbcUtils {

    /**
     * @param objects The entities to close. May be (Connection, ResultSet,
     *            Statement for example)
     */
    public static void closeAllQuietly(Object... objects) {
        for (Object obj : objects) {
            closeQuietly(obj);
        }
    }

    /**
     * Close quietly a {@link Connection}, or a {@link Statement} or a
     * {@link ResultSet}.
     * 
     * @param obj
     *            - The object to close.
     * 
     * @see #closeConnectionQuietly(Connection)
     * @see #closeStatementQuietly(Statement)
     * @see #closeResultsetQuietly(ResultSet)
     */
    public static void closeQuietly(Object obj) {
        if (obj instanceof Connection)
            closeConnectionQuietly((Connection) obj);
        else if (obj instanceof Statement)
            closeStatementQuietly((Statement) obj);
        else if (obj instanceof ResultSet)
            closeResultsetQuietly((ResultSet) obj);
    }

    public static void closeConnectionQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
            }
            conn = null;
        }
    }

    public static void closeStatementQuietly(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
            }
            stmt = null;
        }
    }

    public static void closeResultsetQuietly(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
            }
            rs = null;
        }
    }

    public static void printSQLException(SQLException e) {
        String format = "%-12s : %s\n";
        while (e != null) {
            String errMsg = "------------- SQLException ----------------\n";
            errMsg += String.format(format, "SQL State", e.getSQLState());
            errMsg += String.format(format, "Error Code", e.getErrorCode());
            errMsg += String.format(format, "Cause", e.getCause());
            errMsg += String.format(format, "Message", e.getMessage());
            System.err.print(errMsg);
            // for stack traces, refer to derby.log or uncomment this:
            // e.printStackTrace(System.err);
            System.err
                    .println("--------------------------------------------\n");
            e = e.getNextException();
        }
    }
}
