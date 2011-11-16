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

package net.paissad.waqtsalat.service.conf;

import static net.paissad.waqtsalat.service.WSConstants.NAMESPACE_CONF;

import java.util.HashMap;
import java.util.Map;

import org.jdom.Namespace;

import net.paissad.waqtsalat.service.exception.WSException;

/**
 * @author paissad
 * 
 */
public class DBConf {

    private static final String KEY_DRIVER  = "driver";
    private static final String KEY_DIALECT = "dialect";
    private static final String KEY_URL     = "url";

    private DBConf() {
    }

    private static final String DB_TYPE_MYSQL      = "mysql";
    private static final String DB_TYPE_POSTGRESQL = "postgresql";

    public static String getDBType() throws WSException {
        return GlobalConfiguration.getDBConfElement().getChildTextTrim("db_type",
                Namespace.getNamespace(NAMESPACE_CONF));
    }

    public static String getDBName() throws WSException {
        return GlobalConfiguration.getDBConfElement().getChildTextTrim("db_name",
                Namespace.getNamespace(NAMESPACE_CONF));
    }

    public static String getDBHost() throws WSException {
        return GlobalConfiguration.getDBConfElement().getChildTextTrim("db_host",
                Namespace.getNamespace(NAMESPACE_CONF));
    }

    /**
     * 
     * @return The specified port if any, or <code>null</code> if no port is
     *         specified in the XML configuration file.
     * @throws WSException
     */
    public static Integer getDBPort() throws WSException {
        String port = GlobalConfiguration.getDBConfElement().getChildTextTrim("db_port",
                Namespace.getNamespace(NAMESPACE_CONF));
        return (port == null) ? null : Integer.valueOf(port);
    }

    public static String getDBUser() throws WSException {
        String user = GlobalConfiguration.getDBConfElement().getChildTextTrim("db_user",
                Namespace.getNamespace(NAMESPACE_CONF));
        return (user == null) ? "" : user;
    }

    public static String getDBPassword() throws WSException {
        String pass = GlobalConfiguration.getDBConfElement().getChildTextTrim("db_password",
                Namespace.getNamespace(NAMESPACE_CONF));
        return (pass == null) ? "" : pass;
    }

    public static String getJdbcURL() throws WSException {
        return computeProperties().get(KEY_URL);
    }

    public static String getHibernateDialect() throws WSException {
        return computeProperties().get(KEY_DIALECT);
    }

    public static String getJdbcDriver() throws WSException {
        return computeProperties().get(KEY_DRIVER);
    }

    private static Map<String, String> computeProperties() throws WSException {

        String driver = null;
        String dialect = null;
        StringBuilder url = new StringBuilder();
        final String dbType = getDBType();

        if (DB_TYPE_MYSQL.equalsIgnoreCase(dbType)) {
            driver = "com.mysql.jdbc.Driver";
            dialect = "org.hibernate.dialect.MySQLDialect";
            url.append("jdbc:mysql://");

        } else if (DB_TYPE_POSTGRESQL.equalsIgnoreCase(dbType)) {
            driver = "org.postgresql.Driver"; // PostgreSQL >= 7.x
            dialect = "org.hibernate.dialect.PostgreSQLDialect";
            url.append("jdbc:postgresql://");

        } else {
            throw new IllegalStateException("Unknown database type, unable to get the dialect.");
        }

        url.append(getDBHost());
        if (getDBPort() != null) {
            url.append(":").append(getDBPort());
        }
        url.append("/").append(getDBName());

        Map<String, String> result = new HashMap<String, String>();
        result.put(KEY_DRIVER, driver);
        result.put(KEY_DIALECT, dialect);
        result.put(KEY_URL, url.toString());
        return result;
    }

}
