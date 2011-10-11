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

import static net.paissad.waqtsalat.service.WSConstants.WORLDCITIES_CSV_FILENAME;
import static net.paissad.waqtsalat.service.WSConstants.WORLDCITIES_TABLE_NAME;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.paissad.waqtsalat.service.Coordinates;
import net.paissad.waqtsalat.service.factory.DBConnection;
import net.paissad.waqtsalat.service.utils.CommonUtils;
import net.paissad.waqtsalat.service.utils.JdbcUtils;

/**
 * This class contains utilities to create a database from a worldcitiespop.txt
 * file.
 * 
 * @author Papa Issa DIAKHATE (paissad)
 */
public class WorldCitiesDB {

    private static Logger       logger                  = LoggerFactory.getLogger(WorldCitiesDB.class);

    private static final String CSV_CHARSET             = "ISO-8859-15";

    private static final String SQL_DROP_TABLE          = new StringBuilder()
                                                                .append("DROP TABLE IF EXISTS ")
                                                                .append(WORLDCITIES_TABLE_NAME)
                                                                .append(" CASCADE CONSTRAINTS;")
                                                                .toString();

    private static final String SQL_CREATE_TABLE        = new StringBuilder()
                                                                .append("CREATE TABLE IF NOT EXISTS ")
                                                                .append(WORLDCITIES_TABLE_NAME)
                                                                .append(" (")
                                                                .append("country_code VARCHAR(2)   NOT NULL, ")
                                                                .append("city         VARCHAR(40)  NOT NULL, ")
                                                                .append("region       VARCHAR (3)  NOT NULL, ")
                                                                .append("latitude     FLOAT        NOT NULL, ")
                                                                .append("longitude    FLOAT        NOT NULL, ")
                                                                .append("UNIQUE (country_code, city, latitude, longitude) ")
                                                                .append(" ) AS SELECT COUNTRY, ACCENTCITY, REGION, LATITUDE, LONGITUDE ")
                                                                .append("FROM CSVREAD ('")
                                                                .append(WORLDCITIES_CSV_FILENAME).append("', null, '")
                                                                .append(CSV_CHARSET)
                                                                .append("') WHERE POPULATION IS NOT NULL;")
                                                                .toString();

    private static final String SQL_ALTER_TABLE         = new StringBuilder()
                                                                .append("ALTER TABLE ").append(WORLDCITIES_TABLE_NAME)
                                                                .append(" ADD country_name VARCHAR(40) BEFORE city;")
                                                                .toString();

    private static final String SQL_UPDATE_COUNTRY_CODE = new StringBuilder()
                                                                .append("UPDATE ").append(WORLDCITIES_TABLE_NAME)
                                                                .append(" SET country_code = UPPER(country_code);")
                                                                .toString();

    private static final String SQL_UPDATE_COUNTRY_NAME = new StringBuilder()
                                                                .append("UPDATE ")
                                                                .append(WORLDCITIES_TABLE_NAME)
                                                                .append(" SET country_name = ? WHERE country_code LIKE ?;")
                                                                .toString();

    // =========================================================================

    /**
     * Creates the database/table if it did not exist yet.
     * 
     * @throws IOException
     * @throws SQLException
     */
    public void createTable() throws IOException, SQLException {

        long startTime = System.currentTimeMillis();

        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DBConnection.getInstance();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();

            stmt.execute(SQL_DROP_TABLE);
            stmt.execute(SQL_CREATE_TABLE);
            logger.info("The table '{}' is created successfully.", WORLDCITIES_TABLE_NAME);

            stmt.execute(SQL_ALTER_TABLE);
            logger.debug("The table '{}' is altered successfully.", WORLDCITIES_TABLE_NAME);

            stmt.execute(SQL_UPDATE_COUNTRY_CODE);
            logger.debug("Finished putting country codes in uppercase.");

            conn.commit();

            long endTime = System.currentTimeMillis();
            logger.debug("Database '{}' created in {} seconds.", WORLDCITIES_TABLE_NAME,
                    (int) (endTime - startTime) / 1000);

        } catch (SQLException sqle) {
            String errMsg = "An error occured while creating the table (" + WORLDCITIES_TABLE_NAME + ")\n";
            logger.error(errMsg, sqle);
            JdbcUtils.printSQLException(sqle);
            if (conn != null && !conn.isClosed()) {
                conn.rollback();
            }
            throw new SQLException(errMsg, sqle);

        } finally {
            JdbcUtils.closeAllQuietly(conn, stmt);
        }
    }

    // =========================================================================

    /**
     * Update the country names entries of the database using the default
     * {@link Locale}.
     * 
     * @throws SQLException
     */
    public void updateTableCountryName() throws SQLException {
        updateTableCountryName(Locale.getDefault());
    }

    // =========================================================================

    /**
     * Update the country names entries of the database using the specified
     * {@link Locale}.
     * 
     * @param aLocale
     *            The <code>Locale</code> to use.
     * @throws SQLException
     * 
     */
    public void updateTableCountryName(Locale aLocale) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBConnection.getInstance();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQL_UPDATE_COUNTRY_NAME);
            Map<String, String> countries = CommonUtils.getCountries(aLocale);
            Iterator<Entry<String, String>> it = countries.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, String> entry = it.next();
                String cc = entry.getKey(); // cc like Country Code
                pstmt.setString(1, countries.get(cc));
                pstmt.setString(2, cc);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            logger.info("Finished updating the country names into table '{}' using Locale '{}'.",
                    WORLDCITIES_TABLE_NAME, aLocale.getDisplayName());

            conn.commit();

        } catch (SQLException sqle) {
            String errMsg = "Error while updating names of countries into the database.\n";
            logger.error(errMsg, sqle);
            JdbcUtils.printSQLException(sqle);
            throw new SQLException(errMsg, sqle);

        } finally {
            JdbcUtils.closeQuietly(conn);
            JdbcUtils.closeQuietly(pstmt);
        }
    }

    // =========================================================================

    /**
     * @param country
     *            The country.
     * @param city
     *            The city.
     * @return The {@link Coordinates} of the couple <i>city/country</i>, return
     *         <code>null</code> if the couple of city/country is not found into
     *         the database or when an error occurs.
     * @throws SQLException
     * 
     */
    public static Coordinates getCoordinatesFromCountryAndCity(String country, String city) throws SQLException {

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getInstance();
            Coordinates coord = null;
            String sql = "SELECT latitude, longitude FROM " + WORLDCITIES_TABLE_NAME
                    + " WHERE country_name LIKE '" + country + "' AND city LIKE '" + city + "';";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) { // We only select the 1st row !
                coord = new Coordinates();
                coord.setLatitude(rs.getFloat("latitude"));
                coord.setLongitude(rs.getFloat("longitude"));
            }
            return coord;

        } catch (SQLException sqle) {
            String errMsg = "Error while retrieving the coordinates of (" + country + ", " + city
                    + ") from the database.";
            logger.error(errMsg, sqle);
            JdbcUtils.printSQLException(sqle);
            throw new SQLException(errMsg, sqle);

        } finally {
            JdbcUtils.closeQuietly(conn);
            JdbcUtils.closeQuietly(stmt);
            JdbcUtils.closeQuietly(rs);
        }
    }

    // =========================================================================

    /**
     * Get the country ISO Code from a given country name.
     * 
     * @param countryName
     *            The full name of the country.
     * 
     * @return The String 2 letters representation of the country code in
     *         upper case.
     * @throws SQLException
     */
    public static String getCountryCodeFromCountryName(String countryName) throws SQLException {
        Connection conn = null;
        ResultSet rs = null;
        Statement stmt = null;
        try {
            conn = DBConnection.getInstance();
            conn.setAutoCommit(false);

            String cc = null; // cc like the abbreviation of country code
            if (countryName != null) {
                String sql = "SELECT country_code FROM " + WORLDCITIES_TABLE_NAME
                        + " WHERE country_name LIKE '" + countryName + "';";
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);
                if (rs.next()) { // We only select the 1st row ...
                    cc = rs.getString(1);
                }
            }
            return cc;

        } catch (SQLException sqle) {
            String errMsg = "Error while getting country code for (" + countryName + ") from the database.";
            logger.error(errMsg, sqle);
            JdbcUtils.printSQLException(sqle);
            throw new SQLException(errMsg, sqle);

        } finally {
            JdbcUtils.closeQuietly(conn);
            JdbcUtils.closeQuietly(stmt);
            JdbcUtils.closeQuietly(rs);
        }
    }

    // =========================================================================

}
