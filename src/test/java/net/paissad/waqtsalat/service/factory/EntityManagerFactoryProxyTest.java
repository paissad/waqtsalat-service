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

import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import net.paissad.waqtsalat.service.Constants;
import net.paissad.waqtsalat.service.conf.GlobalConfiguration;

/**
 * @author paissad
 * 
 */
public class EntityManagerFactoryProxyTest {


    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Assert.assertTrue(Constants.CONF_FILE_OK.isFile());
        GlobalConfiguration.loadConfigFile(Constants.CONF_FILE_OK);
    }

    /**
     * Test method for
     * {@link net.paissad.waqtsalat.service.factory.EntityManagerFactoryProxy#getInstance()}
     * .
     */
    @Test
    public final void testGetInstance() {
        EntityManager em = EntityManagerFactoryProxy.getInstance();
        Assert.assertNotNull("The entity manager should not be null !", em);
    }

}
