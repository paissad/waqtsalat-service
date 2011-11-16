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

import java.io.File;

import org.apache.xerces.parsers.SAXParser;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.paissad.waqtsalat.service.exception.WSException;
import net.paissad.waqtsalat.service.utils.XMLUtils;

/**
 * @author paissad
 * 
 */
public class GlobalConfiguration {

    /** The prefix that will be used to bind to the default namespace */
    public static final String PREFIX = "w";

    private static Logger      logger = LoggerFactory.getLogger(GlobalConfiguration.class);

    private static File        configFile;
    private static Document    document;

    private GlobalConfiguration() {
    }

    private static void parseConfiguration() throws WSException {

        try {
            if (getConfigFile() == null || !getConfigFile().isFile()) {
                throw new IllegalStateException("The configuration is null or does not exist or is not a file.");
            }

            SAXBuilder sb = new SAXBuilder(SAXParser.class.getName(), true);
            sb.setFeature("http://apache.org/xml/features/validation/schema", true);
            GlobalConfiguration.document = sb.build(getConfigFile());

        } catch (Exception e) {
            String errMsg = "Error while parsing the configuration file.";
            logger.error(errMsg, e);
            throw new WSException(errMsg, e);
        }
    }

    /**
     * 
     * @return The current configuration file which is used by the application.
     */
    private final static File getConfigFile() {
        return configFile;
    }

    /**
     * <b>NOTE</b>: This method should be called eagerly when the application
     * starts.
     * 
     * @param configFile - The configuration file to use for the application.
     * @throws WSException If an error occurs while loading/parsing the file.
     */
    public final static synchronized void loadConfigFile(final File configFile) throws WSException {
        GlobalConfiguration.configFile = configFile;
        parseConfiguration();
    }

    /**
     * @return The {@link Element} which holds the database configuration (the
     *         'db_conf' tag normally)
     * @throws WSException
     */
    static Element getDBConfElement() throws WSException {
        // /*[local-name() = 'waqtsalat-service']/*[local-name() = 'db_conf']
        try {
            return (Element) XMLUtils.selectNodeHavingPath(document,
                    "/" + PREFIX + ":waqtsalat-service/" + PREFIX + ":db_conf", PREFIX);
        } catch (Exception e) {
            throw new WSException("Unable to retrieve the xml tag element 'db_conf'", e);
        }
    }

}
