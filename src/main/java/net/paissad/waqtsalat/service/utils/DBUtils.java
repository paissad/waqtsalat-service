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

import static net.paissad.waqtsalat.service.WSConstants.WORLDCITIES_TABLE_NAME;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.paissad.waqtsalat.service.factory.DBConnection;

/**
 * @author paissad
 * 
 */
public class DBUtils {

    private static Logger       logger                       = LoggerFactory.getLogger(DBUtils.class);

    public static final int     STATE_ALL_TABLES_EXIST       = 0;
    public static final int     STATE_NO_TABLES_EXIST        = 1;
    public static final int     STATE_MISSING_TABLES         = 2;

    private static final String CSV_SEPARATOR                = ",";

    private static final String SQL_INSERT_WORLDCITIES_TABLE = new StringBuilder()
                                                                     .append("INSERT INTO ")
                                                                     .append(WORLDCITIES_TABLE_NAME)
                                                                     .append(" (country_code, country_name, city, region, latitude, longitude) VALUES (?, ?, ?, ?, ?, ?)")
                                                                     .toString();

    private DBUtils() {
    }

    /**
     * @param conn - The database connection to use. <b>NOTE</b>: The connection
     *            is not closed during the use of this method.
     * @param csvFile - The 'worldcitiespop.txt' file from which read the
     *            entries to add to the database.
     * @throws Exception
     * 
     */
    public static void initializeDatabase(final Connection conn, final File csvFile) throws Exception {

        if (conn == null) {
            throw new IllegalArgumentException("The database connection may not be null.");
        }

        logger.info("Verifying tables ...");
        final int state = verifyTables(conn);
        if (state == STATE_ALL_TABLES_EXIST) {
            return;

        } else if (state == STATE_MISSING_TABLES) {
            logger.info("Some tables are missing, no initialization will be performed, please check your database ...");
            return;

        } else if (state == STATE_NO_TABLES_EXIST) {

            Statement stmt = null;
            Locale locale = Locale.US;
            try {

                logger.info("Initialization of the database ...");
                stmt = conn.createStatement();

                logger.info("Creation of tables ...");
                createTables(stmt);
                logger.info("The tables are created successfully ...");

                logger.debug("Populating the table '{}'", WORLDCITIES_TABLE_NAME);
                populateWorldCitiesTable(conn, csvFile, locale);
                logger.info("The table '{}' is populated successfully !", WORLDCITIES_TABLE_NAME);

                logger.info("The table '{}' is initialized with success ...", WORLDCITIES_TABLE_NAME);

            } catch (Exception e) {
                String errMsg = "Error while initializing the database.\n";
                logger.error(errMsg, e);
                throw new Exception(errMsg, e);

            } finally {
                JdbcUtils.closeAllQuietly(stmt);
            }
        }
    }

    /**
     * 
     * @param conn - The database connection to use.
     * @return <ul>
     *         <li>{@link #STATE_ALL_TABLES_EXIST} if all tables do exist</li>
     *         <li>{@link #STATE_NO_TABLES_EXIST} if no tables exist yet</li>
     *         <li>{@link #STATE_MISSING_TABLES} if some tables exist, but other
     *         are missing.</li>
     *         </ul>
     * @throws IOException
     * @throws SQLException
     */
    public static int verifyTables(final Connection conn) throws IOException, SQLException {

        int expectedTableCount = 0;
        int foundTablesCount = 0;

        String content = readFileFromJar("/conf/sql/tables_names.txt");
        for (String line : content.split("\n")) {
            if (!line.matches("^\\s*#.*") && !line.matches("^\\s*$")) {
                expectedTableCount++;
                if (tableExist(conn, line.trim().toUpperCase(Locale.US))) {
                    foundTablesCount++;
                }
            }
        }
        if (expectedTableCount == foundTablesCount) {
            logger.info("All expected tables do exist.");
            return STATE_ALL_TABLES_EXIST;

        } else if (foundTablesCount == 0) {
            logger.info("No tables do exist yet !");
            return STATE_NO_TABLES_EXIST;

        } else if (foundTablesCount < expectedTableCount) {
            logger.info("Expected {} tables, but found only {} !!! it's abnormal !", expectedTableCount, foundTablesCount);
            return STATE_MISSING_TABLES;

        } else {
            String errMsg = String.format(
                    "Unknow state: number of tables expected -> %d, number of tables found -> %d.",
                    expectedTableCount, foundTablesCount);
            logger.warn(errMsg);
            throw new IllegalStateException(errMsg);
        }
    }

    /**
     * Checks whether or not a table exist.
     * 
     * @param conn
     * @param tableName - The name of the table for which we want to verify its
     *            existence.
     * @return {@code true} if the table exists, {@code false} otherwise.
     * @throws SQLException
     */
    private static boolean tableExist(final Connection conn, final String tableName) throws SQLException {
        if (conn == null) {
            throw new IllegalArgumentException("The database connection may not be null.");
        }
        ResultSet tables = null;
        try {
            DatabaseMetaData dbm = conn.getMetaData();
            tables = dbm.getTables(null, null, tableName, null);
            return tables.next();
        } finally {
            JdbcUtils.closeAllQuietly(tables);
        }
    }

    /**
     * @param locale - The {@link Locale} to use.
     * @return A {@link Map} containing all ISO country codes as keys and
     *         all countries names as values.
     */
    private static Map<String, String> getCountryNames(final Locale locale) {

        Map<String, String> countries = new HashMap<String, String>();
        String[] allISOCountries = Locale.getISOCountries();
        String lang = locale.getLanguage();
        for (String countryCode : allISOCountries) {
            String countryName = new Locale(lang, countryCode).getDisplayCountry(locale);
            countries.put(countryCode.toUpperCase(), countryName);
        }
        return countries;
    }

    /**
     * 
     * @param conn - The database connection.
     * @param csvFile - The 'worldcitiespop.txt' file
     * @param locale
     * @throws SQLException
     * @throws IOException
     */
    private static void populateWorldCitiesTable(final Connection conn, final File csvFile, final Locale locale)
            throws SQLException, IOException {

        if (conn == null) {
            throw new IllegalArgumentException("The database connection may not be null.");
        }
        PreparedStatement pstmt = null;
        RandomAccessFile raf = null;
        try {
            Map<String, Integer> headers = new HashMap<String, Integer>();
            raf = new RandomAccessFile(csvFile, "rw");
            FileChannel channel = raf.getChannel();
            FileLock lock = channel.lock();
            // We read the first line which holds the names of the headers ...
            String headerLine = raf.readLine();
            if (headerLine == null || headerLine.isEmpty()) {
                throw new IllegalStateException("The header line of the file " + csvFile + " is null or empty.");
            } else {
                String[] headerFields = headerLine.split(CSV_SEPARATOR);
                for (int i = 0; i < headerFields.length; i++) {
                    headers.put(headerFields[i].toUpperCase(Locale.US), i);
                }
            }
            pstmt = conn.prepareStatement(SQL_INSERT_WORLDCITIES_TABLE);
            Map<String, String> countries = getCountryNames(locale);
            // Now we read each row and add it to the result set !
            String line;
            while ((line = raf.readLine()) != null) {
                String[] values = line.split(CSV_SEPARATOR);
                String population = values[headers.get("POPULATION")];
                if (population != null && !population.isEmpty()) {
                    String countryCode = values[headers.get("COUNTRY")].toUpperCase();
                    pstmt.setString(1, countryCode);
                    pstmt.setString(2, countries.get(countryCode));
                    pstmt.setString(3, values[headers.get("ACCENTCITY")]);
                    pstmt.setString(4, values[headers.get("REGION")]);
                    pstmt.setFloat(5, Float.valueOf(values[headers.get("LATITUDE")]));
                    pstmt.setFloat(6, Float.valueOf(values[headers.get("LONGITUDE")]));
                    pstmt.addBatch();
                }
            }
            lock.release();
            channel.close();
            pstmt.executeBatch();
            conn.commit();

        } finally {
            JdbcUtils.closeAllQuietly(pstmt);
            CommonUtils.closeAllStreamsQuietly(raf);
        }
    }

    private static void createTables(final Statement stmt) throws IOException, SQLException {
        String sql = readFileFromJar("/conf/sql/create_tables.sql");
        try {
            stmt.execute(sql);
        } catch (SQLException sqle) {
            String errMsg = "Error while creating tables.\n";
            logger.error(errMsg, sqle);
            JdbcUtils.printSQLException(sqle);
            throw new SQLException(errMsg, sqle);
        }
    }

    /**
     * 
     * @param conn - The connection to use. <b>NOTE</b>: Is not closed during
     *            the use of this method.
     * @throws IOException
     * @throws SQLException
     */
    public static void dropTables(final Connection conn) throws IOException, SQLException {
        if (conn == null || conn.isClosed() || conn.isReadOnly()) {
            throw new IllegalArgumentException("The connection cannot be null, or closed or read-only !");
        }
        String sql = readFileFromJar("/conf/sql/drop_tables.sql");
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException sqle) {
            String errMsg = "Error while dropping tables.\n";
            logger.error(errMsg, sqle);
            JdbcUtils.printSQLException(sqle);
            throw new SQLException(errMsg, sqle);
        } finally {
            JdbcUtils.closeStatementQuietly(stmt);
        }
    }

    private static String readFileFromJar(final String path) throws IOException {
        InputStream in = DBUtils.class.getResourceAsStream(path);
        if (in == null) {
            throw new IllegalStateException("The inputstream of the resource file '" + path + "' cannot be null !");
        }
        LineNumberReader reader = new LineNumberReader(new InputStreamReader(in, Charset.forName("utf-8")));
        StringBuilder sql = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sql.append(line).append("\n");
        }
        return sql.toString();
    }

    /*
     * FOR TESTING PURPOSE ONLY ! XXX
     */
    public static void main(String[] args) throws Exception {
        File csvFile = new File("worldcitiespop.txt");
        Connection conn = DBConnection.getInstance();

        logger.info("Droping tables ...");
        dropTables(conn);
        logger.info("Dropped tables successfully !");

        initializeDatabase(conn, csvFile);
        conn.close();
    }
}
