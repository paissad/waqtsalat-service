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

package net.paissad.waqtsalat;

import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;

/**
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
public class TestUtils {

    /**
     * Checks basically whether or not internet connection is available.
     * 
     * @return <code>true</code> if the internet connection is OK,
     *         <code>false</code> otherwise.
     */
    public static boolean isInternetConnectionOK() {
        try {
            boolean ok1 = pingUsingInetAddress();
            boolean ok2 = pingURL("http://yahoo.com") ||
                    pingURL("http://google.com");
            if (ok1 && !ok2) {
                System.err.println(
                        "Ping to 4.2.2.2 succeeded, but ping to Minus and Google failed, maybe DNS issues ?");
            }
            return ok1 || ok2;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean pingURL(final String url) {
        URLConnection conn = null;
        try {
            URL u = new URL(url);
            conn = u.openConnection();
            // Set a timeout of 15 seconds
            conn.setReadTimeout((int) TimeUnit.SECONDS.toMillis(15));
            conn.setAllowUserInteraction(false);
            // conn.setRequestMethod("HEAD");
            conn.connect();
            // return (conn.getResponseCode() == HttpURLConnection.HTTP_OK);
            return true;

        } catch (Exception e) {
            return false;
        } finally {
            if (conn != null) {
                // conn.disconnect();
            }
        }
    }

    private static boolean pingUsingInetAddress() {
        try {
            return InetAddress.getByName("4.2.2.2").isReachable((int) TimeUnit.SECONDS.toMillis(15));
        } catch (Exception e) {
            return false;
        }
    }

}
