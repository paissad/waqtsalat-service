package net.paissad.waqtsalat.servlet.listener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.paissad.waqtsalat.conf.ConfigHelper;
import net.paissad.waqtsalat.dao.impl.RoleDAO;
import net.paissad.waqtsalat.domain.Role;
import net.paissad.waqtsalat.enums.RoleType;

@WebListener
public class ServletContextListenerImpl implements ServletContextListener {

    private static Logger       logger               = LoggerFactory.getLogger(ServletContextListenerImpl.class);

    private static final String JNDI_CONFIG_FILEPATH = "file/__waqtsalat-service-config";

    @Override
    public void contextInitialized(final ServletContextEvent sce) {
        try {
            logger.info("Starting WaqtSalat-Service Web application.");

            System.getProperties().put("org.apache.el.parser.COERCE_TO_ZERO", "false");

            final Context initCtx = new InitialContext();
            final Context envCtx = (Context) initCtx.lookup("java:comp/env");
            String configFilePath = null;
            try {
                configFilePath = (String) envCtx.lookup(JNDI_CONFIG_FILEPATH);
            } catch (final NameNotFoundException e) {
                // Do nothing if the JNDI is not set.
            }

            final File internalConfigFile = new File(sce.getServletContext().getRealPath("/"),
                    "WEB-INF/waqtsalat-service.conf");

            final File configFile = (configFilePath == null || configFilePath.trim().isEmpty()) ? internalConfigFile
                    : new File(configFilePath);

            logger.debug("The configuration file to use is '{}'", configFile);

            if (!configFile.isFile()) {
                final String errMsg = "The configuration file '" + configFile + "' does not exist or is not file.";
                throw new FileNotFoundException(errMsg);
            }
            if (!configFile.canRead()) {
                final String errMsg = "The configuration file '" + configFile + "' is not readable.";
                throw new IOException(errMsg);
            }

            logger.debug("Initializing settings from configuration file '{}'", configFile);
            ConfigHelper.initialize(configFile);
            logger.debug("Settings retrieved successfully from '{}'", configFile);

            // Populating ws_roles table.
            final RoleDAO roleDAO = new RoleDAO();
            for (final RoleType type : RoleType.values()) {
                if (roleDAO.findByType(type) == null) {
                    roleDAO.create(new Role(type));
                }
            }

            logger.info("WaqtSalat-Service Web application started successfully.");

        } catch (final Exception e) {
            final String errMsg = "Error while starting the web application";
            logger.error(errMsg, e);
            throw new IllegalStateException(errMsg, e);
        }
    }

    @Override
    public void contextDestroyed(final ServletContextEvent sce) {
        logger.info("WaqtSalat-Service Web application stopped.");
    }

}
