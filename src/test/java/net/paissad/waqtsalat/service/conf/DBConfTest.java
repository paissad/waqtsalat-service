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

import static net.paissad.waqtsalat.service.Constants.CONF_FILE_OK;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import net.paissad.waqtsalat.service.exception.WSException;

/**
 * @author paissad
 * 
 */
public class DBConfTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Assert.assertTrue(CONF_FILE_OK.isFile());
        GlobalConfiguration.loadConfigFile(CONF_FILE_OK);
    }

    @Test
    public final void testGetDBType() throws WSException {
        Assert.assertEquals("mysql", DBConf.getDBType());
    }

    @Test
    public final void testGetDBName() throws WSException {
        Assert.assertEquals("ws_service_test", DBConf.getDBName());
    }

    @Test
    public final void testGetDBHost() throws WSException {
        Assert.assertEquals("localhost", DBConf.getDBHost());
    }

    @Test
    public final void testGetDBPort() throws WSException {
        Assert.assertEquals(new Integer(3306), DBConf.getDBPort());
    }

    @Test
    public final void testGetDBUser() throws WSException {
        Assert.assertEquals("root", DBConf.getDBUser());
    }

    @Test
    public final void testGetDBPassword() throws WSException {
        Assert.assertEquals("root", DBConf.getDBPassword());
    }

}
