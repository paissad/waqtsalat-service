package net.paissad.waqtsalat.conf;

import java.util.Locale;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBConf {

    private static Logger               logger                = LoggerFactory.getLogger(DBConf.class);

    private static final String         PERSISTENCE_UNIT_NAME = "waqtsalat-service";

    private static EntityManagerFactory emf;

    private static boolean              emfInitialized        = false;

    private static enum DB_TYPE {

        MYSQL, POSTGRESQL;

        static boolean isSupported(final String val) {
            final DB_TYPE dbType = DB_TYPE.valueOf(val);
            boolean found = false;
            for (final DB_TYPE type : DB_TYPE.values()) {
                if (type.equals(dbType)) {
                    found = true;
                    break;
                }
            }
            return found;
        }
    }

    private DBConf() {
    }

    private static DB_TYPE getType() {
        final String val = ConfigHelper.getValue("db.type");
        if (val == null || val.trim().isEmpty() || !DB_TYPE.isSupported(val)) {
            final String errMsg = "The value of the database type 'db.type' must be set to a correct and known value";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        }
        return DB_TYPE.valueOf(val);
    }

    private static String getHibernateDialect() {
        switch (getType()) {
        case MYSQL:
            return "org.hibernate.dialect.MySQLDialect";
        case POSTGRESQL:
            return "org.hibernate.dialect.PostgreSQLDialect";
        default:
            throw new IllegalArgumentException("Unknown database type.");
        }
    }

    private static String getDriver() {
        switch (getType()) {
        case MYSQL:
            return "com.mysql.jdbc.Driver";
        case POSTGRESQL:
            return "org.postgresql.Driver";
        default:
            throw new IllegalArgumentException("Unknown database type.");
        }
    }

    private static String getHostname() {
        return ConfigHelper.getValue("db.hostname", "localhost");
    }

    private static String getPort() {
        return ConfigHelper.getValue("db.port");
    }

    private static String getUsername() {
        return ConfigHelper.getValue("db.username");
    }

    private static String getPassword() {
        return ConfigHelper.getValue("db.password");
    }

    private static String getDatabase() {
        final String val = ConfigHelper.getValue("db.name");
        if (val == null || val.trim().isEmpty()) {
            final String errMsg = "The name of the database must be set.";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        }
        return val;
    }

    private static String getJdbcURL() {
        final StringBuilder sb = new StringBuilder("jdbc:");
        final String dbType = getType().toString().toLowerCase(Locale.ENGLISH);
        sb.append(dbType).append("://").append(getHostname());
        final String port = getPort();
        if (port != null && !port.trim().isEmpty()) {
            sb.append(":").append(port);
        }
        sb.append("/").append(getDatabase());
        return sb.toString();
    }

    /**
     * @return The persistence properties to use for {@link EntityManagerFactory}. (JPA/Hibernate)
     */
    private static Properties getPersistenceProperties() {
        final Properties properties = new Properties();
        properties.put("javax.persistence.jdbc.user", getUsername());
        properties.put("javax.persistence.jdbc.password", getPassword());
        properties.put("javax.persistence.jdbc.driver", getDriver());
        properties.put("javax.persistence.jdbc.url", getJdbcURL());
        properties.put("hibernate.dialect", getHibernateDialect());
        return properties;
    }

    private static synchronized void initializeEMF() {
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, getPersistenceProperties());
        emfInitialized = true;
    }

    /**
     * @return An instance of {@link EntityManager}.
     */
    public static EntityManager getEntityManager() {
        if (!emfInitialized) initializeEMF();
        return emf.createEntityManager();
    }
}
