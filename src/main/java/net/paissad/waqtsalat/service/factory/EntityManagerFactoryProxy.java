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

package net.paissad.waqtsalat.service.factory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.paissad.waqtsalat.service.conf.DBConf;
import net.paissad.waqtsalat.service.conf.GlobalConfiguration;
import net.paissad.waqtsalat.service.exception.WSException;

/**
 * @author paissad
 * 
 */
public class EntityManagerFactoryProxy {

    private static Logger              logger                = LoggerFactory.getLogger(EntityManagerFactoryProxy.class);

    private static final String        PERSISTENCE_UNIT_NAME = "waqtsalat-service";
    private static Map<String, String> propertiesOverrides;

    private static boolean             initialized           = false;

    static {
        try {
            propertiesOverrides = new HashMap<String, String>();
            logger.info("Initializing the database configuration.");

            // XXX changer le fichier de configuration
            GlobalConfiguration.loadConfigFile(new File("/Users/paissad/Documents/workspace/waqtsalat-service/waqtsalat-service.xml"));
            
            propertiesOverrides.put("hibernate.connection.driver_class", DBConf.getJdbcDriver());
            propertiesOverrides.put("hibernate.dialect", DBConf.getHibernateDialect());
            propertiesOverrides.put("hibernate.connection.username", DBConf.getDBUser());
            propertiesOverrides.put("hibernate.connection.password", DBConf.getDBPassword());
            propertiesOverrides.put("hibernate.connection.url", DBConf.getJdbcURL());

            initialized = true;
            logger.info("Configuration initialized successfully !");

        } catch (WSException e) {
            logger.error("Error while initializing the database configuration : ", e);
        }
    }

    private EntityManagerFactoryProxy() {
    }

    /**
     * @return One instance of {@link EntityManager}.
     * @throws IllegalStateException If the database configuration is not
     *             initialized yet.
     *             !
     */
    public static EntityManager getInstance() throws IllegalStateException {

        if (!initialized) {
            throw new IllegalStateException("The database configuration is not initialized yet, " +
                    "verify that the configuration file and the credentials are correct and restart the application.");
        }
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, propertiesOverrides);
        return emf.createEntityManager();
    }

}
