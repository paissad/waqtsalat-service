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

import java.text.ParseException;
import java.util.Calendar;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author paissad
 * 
 */
public class TimeBeanTest {

    private static final int START_YEAR = 2011;

    /**
     * Test method for
     * {@link net.paissad.waqtsalat.service.bean.TimeBean#getCopyrightDate()}.
     * 
     * @throws ParseException
     */
    @Test
    public final void testGetCopyrightDate() throws ParseException {
        TimeBean timeBean = new TimeBean();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String copyrightDate = timeBean.getCopyrightDate();
        Assert.assertNotNull(copyrightDate);
        if (currentYear == START_YEAR) {
            Assert.assertEquals(String.valueOf(START_YEAR), copyrightDate);
        } else {
            Assert.assertEquals(String.valueOf(START_YEAR) + "-" + currentYear, copyrightDate);
        }
    }

}
