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

package net.paissad.waqtsalat.service;

import java.util.Locale;

/**
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
public interface WSConstants {

    // General settings ...
    String WS_NAME                    = "WaqtSalat-Service";
    String WS_MAJOR_VERSION           = "0";
    String WS_MINOR_VERSION           = "0";
    String WS_MICRO_VERSION           = "1-beta";
    String WS_VERSION                 = new StringBuilder()
                                              .append(WS_MAJOR_VERSION + ".")
                                              .append(WS_MINOR_VERSION + ".")
                                              .append(WS_MICRO_VERSION)
                                              .toString();

    String WS_DESCRIPTION             = "Application for computing muslim pray times in many cities.";
    String WS_AUTHOR                  = "Papa Issa DIAKHATE (paissad)";
    String WS_COPYRIGHT               = "Copyright (C) 2011 " + WS_AUTHOR;

    // Logger settings ...
    String WS_DEFAULT_LOG_FILENAME    = "waqtsalat-service.log";
    String WS_DEFAULT_MAX_LOG_SIZE    = "10MB";

    // System specifics settings ...
    String OS_NAME                    = System.getProperty("os.name");
    String OS_ARCH                    = System.getProperty("os.arch");
    String OS_VERSION                 = System.getProperty("os.version");

    // JVM specifics settings ...
    String JVM_NAME                   = System.getProperty("java.vm.name");
    String JAVA_VERSION               = System.getProperty("java.version");
    String JAVA_VENDOR                = System.getProperty("java.vendor");

    String FILE_SEP                   = System.getProperty("file.separator");
    String PATH_SEP                   = System.getProperty("path.separator");
    String LINE_SEP                   = System.getProperty("line.separator");

    int    EXIT_SUCCESS               = 0;
    int    EXIT_ERROR                 = 1;

    String WS_USER_AGENT              = new StringBuilder()
                                              .append(WS_NAME + "/")
                                              .append(WS_VERSION + " (")
                                              .append(OS_NAME + "; U ")
                                              .append(OS_ARCH + "; ")
                                              .append(OS_VERSION + "; ")
                                              .append(Locale.getDefault())
                                              .append(") Java/")
                                              .append(JAVA_VERSION)
                                              .toString();

    // GEOIP settings ...
    String GEOIP_DATABASE_FILENAME    = "GeoLiteCity.dat";
    String GEOIP_DATABASE_UPDATE_URL  = "http://geolite.maxmind.com/download/geoip/database/"
                                              + GEOIP_DATABASE_FILENAME + ".gz";
    String GEOIP_DATABASE_FULLPATH    = "extras/geoip/" + GEOIP_DATABASE_FILENAME;
    String GEOIP_WORLDCITIES_FILENAME = "worldcitiespop.txt";
    String GEOIP_WORLDCITIES_URL      = "http://www.maxmind.com/download/worldcities/"
                                              + GEOIP_WORLDCITIES_FILENAME + ".gz";
    String WORLDCITIES_TABLE_NAME     = "WORLDCITIES";
    String WORLDCITIES_CSV_FILENAME   = "extras/geoip/" + GEOIP_WORLDCITIES_FILENAME;
    double LATITUDE_MAKKAH            = 21.42738;
    double LONGITUDE_MAKKAH           = 39.81484;

    // Lucene settings ...
    String LUCENE_INDEX_PATH          = "extras/geoip/lucene-index";

}
