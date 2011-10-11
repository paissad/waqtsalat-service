/*
 * WaqtSalat, for indicating the muslim prayers times in most cities. Copyright
 * (C) 2011 Papa Issa DIAKHATE (paissad).
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * PLEASE DO NOT REMOVE THIS COPYRIGHT BLOCK.
 */

package net.paissad.waqtsalat.service.geoip;

import static net.paissad.waqtsalat.service.WSConstants.GEOIP_DATABASE_FULLPATH;
import static net.paissad.waqtsalat.service.WSConstants.GEOIP_DATABASE_UPDATE_URL;
import static net.paissad.waqtsalat.service.WSConstants.GEOIP_WORLDCITIES_URL;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;

import net.paissad.waqtsalat.service.Coordinates;
import net.paissad.waqtsalat.service.exception.WSException;
import net.paissad.waqtsalat.service.utils.CommonUtils;
import net.paissad.waqtsalat.service.utils.HttpUtils;

;

/**
 * Some utilities such as looking for updates, or download of GeoIP database.
 * 
 * @author Papa Issa DIAKHATE (paissad)
 */
public class GeoipHelper {

    private static Logger logger = LoggerFactory.getLogger(GeoipHelper.class);

    // _________________________________________________________________________

    public static enum GEOIPTYPE {
        DATABASE,
        WORLD_CITIES
    };

    // _________________________________________________________________________

    public static void download(final GEOIPTYPE type, final File outputFile) throws WSException  {

        String url = null;
        if (type == GEOIPTYPE.DATABASE) {
            url = GEOIP_DATABASE_UPDATE_URL;
        } else if (type == GEOIPTYPE.WORLD_CITIES) {
            url = GEOIP_WORLDCITIES_URL;
        }
        HttpUtils.downloadFile(url, outputFile, null);
    }

    // _________________________________________________________________________

    /**
     * Verify whether or not an update is available for the specified GEOIP
     * resource.
     * <p>
     * <b>Note</b>:If the local file does not exist yet, then an update is
     * supposed to be automatically available.
     * </p>
     * 
     * @param type
     * @param localFile
     *            - The local file to use while comparing timestamps.
     * @return <tt>true</tt> if an update is available, <tt>false</tt>
     *         otherwise.
     * 
     * @see GEOIPTYPE
     */
    public static boolean isUpdateAvailable(GEOIPTYPE type, final File localFile) {

        /*
         * TODO: instead of relying on the presence and timestamp of the local
         * file, the datas should be stored into a database. It's safer.
         * In other words, the timestamp of 'eventual' previous dates of updates
         * should be saved into a database/table.
         */

        logger.info("Checking GeoIP update for {}.", type.toString());
        String url = null;
        if (type == GEOIPTYPE.DATABASE) {
            url = GEOIP_DATABASE_UPDATE_URL;
        } else if (type == GEOIPTYPE.WORLD_CITIES) {
            url = GEOIP_WORLDCITIES_URL;
        }

        if (!localFile.exists()) {
            return true;
        }

        Date localDate = new Date(localFile.lastModified());
        Date remoteDate = new Date(CommonUtils.getRemoteTimestamp(url));

        return remoteDate.after(localDate);
    }

    // _________________________________________________________________________

    /**
     * Get geographic coordinates from a given IP address.
     * 
     * @param ipAddress
     * @return The {@link Coordinates} for the given IP address.
     * @throws IOException
     */
    public static Coordinates getCoordinates(String ipAddress) throws IOException {
        Location location = getLocation(ipAddress);
        return new Coordinates(location.latitude, location.longitude);
    }

    /**
     * Get the city name for the specified IP address.
     * 
     * @param ipAddress
     * @return The city name.
     * @throws IOException
     */
    public static String getCity(String ipAddress) throws IOException {
        Location location = getLocation(ipAddress);
        return location.city;
    }

    /**
     * Get the country name from a given IP address.
     * 
     * @param ipAddress
     * @return The country name.
     * @throws IOException
     */
    public static String getCountry(String ipAddress) throws IOException {
        Location location = getLocation(ipAddress);
        return location.countryName;
    }

    /**
     * @param ipAddress
     * @return The {@link Location} object for the given IP address.
     * @throws IOException
     */
    private static Location getLocation(final String ipAddress) throws IOException {
        LookupService service = new LookupService(
                GEOIP_DATABASE_FULLPATH, LookupService.GEOIP_MEMORY_CACHE);
        return service.getLocation(ipAddress);
    }

    // _________________________________________________________________________

}
