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

import static net.paissad.waqtsalat.service.Constants.CONF_FILE_BAD;
import static net.paissad.waqtsalat.service.Constants.CONF_FILE_OK;

import java.io.File;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import net.paissad.waqtsalat.service.exception.WSException;

/**
 * @author paissad
 * 
 */
public class GlobalConfigurationTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Assert.assertTrue(CONF_FILE_OK.isFile());
        Assert.assertTrue(CONF_FILE_BAD.isFile());
    }

    @Test
    public final void testLoadConfigFile_OK() throws WSException {
        GlobalConfiguration.loadConfigFile(CONF_FILE_OK);
    }

    @Test(expected = WSException.class)
    public final void testLoadConfigFile_BAD() throws WSException {
        GlobalConfiguration.loadConfigFile(CONF_FILE_BAD);
    }

    @Test(expected = WSException.class)
    public final void testLoadConfigFile_NULL() throws WSException {
        GlobalConfiguration.loadConfigFile(null);
    }

    @Test(expected = WSException.class)
    public final void testLoadConfigFile_NON_EXISTING_FILE() throws WSException {
        GlobalConfiguration.loadConfigFile(new File("src/test/resources/abcdefgh_xyz"));
    }

    @Test
    public final void testGetDBConfElement() throws WSException {
        GlobalConfiguration.getDBConfElement();
    }

}
