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

package net.paissad.waqtsalat.service.utils;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import net.paissad.waqtsalat.service.TestUtils;
import net.paissad.waqtsalat.service.exception.WSException;

/**
 * @author paissad
 * 
 */
public class HttpUtilsTest {

    private static final String LICENSE_GPL_URL      = "http://www.gnu.org/licenses/gpl.txt";
    private static final String LICENSE_GPL_FILENAME = "gpl.txt";

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        TestUtils.isInternetConnectionOK();
    }

    @Test
    public final void testGetFilenameFromURL() throws WSException {
        Assert.assertEquals(LICENSE_GPL_FILENAME, HttpUtils.getFilenameFromURL(LICENSE_GPL_URL));
    }

    @Test
    public final void testDownloadFile() throws WSException, IOException {
        File destinationFile = null;
        try {
            destinationFile = File.createTempFile("gpl", ".txt", null);
            HttpUtils.downloadFile(LICENSE_GPL_URL, destinationFile, null);
            Assert.assertNotNull(destinationFile);
            Assert.assertTrue(destinationFile.exists());
        } finally {
            if (destinationFile != null && destinationFile.exists())
                destinationFile.delete();
        }
    }

}
