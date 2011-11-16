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

package net.paissad.waqtsalat.service.bean;

import static org.junit.Assert.fail;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import net.paissad.waqtsalat.service.factory.EntityManagerFactoryProxy;

/**
 * @author paissad
 * 
 */
public class UserTest {

    private EntityManager em;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() {
        em = EntityManagerFactoryProxy.getInstance();
    }

    @After
    public void tearDown() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }

    /**
     * Test method for
     * {@link net.paissad.waqtsalat.service.bean.User#getNickName()}.
     */
    @Test
    public final void testGetNickName() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link net.paissad.waqtsalat.service.bean.User#setNickName(java.lang.String)}
     * .
     */
    @Test
    public final void testSetNickName() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link net.paissad.waqtsalat.service.bean.User#getEmail()}.
     */
    @Test
    public final void testGetEmail() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link net.paissad.waqtsalat.service.bean.User#setEmail(java.lang.String)}
     * .
     */
    @Test
    public final void testSetEmail() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link net.paissad.waqtsalat.service.bean.User#getPassword()}.
     */
    @Test
    public final void testGetPassword() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link net.paissad.waqtsalat.service.bean.User#setPassword(java.lang.String)}
     * .
     */
    @Test
    public final void testSetPassword() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link net.paissad.waqtsalat.service.bean.User#register()}.
     */
    @Test
    public final void testRegister() {
        fail("Not yet implemented"); // TODO
    }

}
