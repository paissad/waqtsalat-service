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

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.paissad.waqtsalat.service.event.BasicProgressEvent;
import net.paissad.waqtsalat.service.listener.ProgressListener;

/**
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
public class CommonUtils {

    private static Logger logger = LoggerFactory.getLogger(CommonUtils.class);

    private CommonUtils() {
    }

    /**
     * Returns the filename extension without the dot.
     * 
     * @param filename
     * @return The extension with the dot included, or <code>null</code> if the
     *         filename is null, or an empty String if the file has no
     *         extension.
     * @throws IllegalArgumentException If the file's name is null.
     */
    public static String getFilenameExtension(final String filename) throws IllegalArgumentException {
        if (filename == null) {
            throw new IllegalArgumentException("The name of the file may not be null.");
        }
        int lastIndexOfDot = filename.lastIndexOf(".");
        return filename.substring(lastIndexOfDot + 1);
    }

    // _________________________________________________________________________

    /**
     * @param bytes
     * @param si
     *            - If <code>true</code> then use 1000 unit, 1024 otherwise.
     * @return A String representation of the file size.
     * 
     */
    public static String humanReadableByteCount(final long bytes, final boolean si) {
        int unit = si ? 1000 : 1024;

        if (bytes < unit)
            return bytes + " B";

        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    // _________________________________________________________________________

    /**
     * @param url
     *            - The url of the resource we want to get its timestamp.
     * @return The timestamp (last modification date) of the specified
     *         url/resource, -1L if an error occurred, 0L if not known.
     */
    public static long getRemoteTimestamp(final String url) {
        try {
            URL u = new URL(url);
            URLConnection urlc = u.openConnection();
            return urlc.getLastModified();

        } catch (Exception e) {
            return -1L;
        }
    }

    // _________________________________________________________________________

    /**
     * Test whether or not we have an internet connection.
     * <p>
     * <span style="color: rgb(51, 204, 0);">This routine is not reliable at
     * all, but can suit for most common cases.</span>
     * </p>
     * 
     * @return <tt>true</tt> if internet connection is available, <tt>false</tt>
     *         otherwise.
     */
    public static boolean isInternetReachable() {
        try {
            URL url = new URL("http://google.com");
            URLConnection urlc = url.openConnection();
            urlc.setConnectTimeout(20 * 1000); // timeout of 20 sec
            urlc.getContent();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // _________________________________________________________________________

    /**
     * @return The IP address of the machine which is running the application,
     *         -1 if no ip is found.
     * @throws IOException
     */
    public static String retreiveIpAddress() throws IOException {

        if (!CommonUtils.isInternetReachable()) {
            logger.warn("It seems like you don't have an internet connection.");
        }

        BufferedReader bf = null;
        try {
            URL url = new URL("http://checkip.dyndns.org");

            bf = new BufferedReader(new InputStreamReader(url.openStream()));
            String ipRegex = " *(((\\d{1,3}\\.){3}\\d{1,3}))";
            StringBuilder sb = new StringBuilder();
            String inputLine;
            while ((inputLine = bf.readLine()) != null) {
                sb.append(inputLine);
            }
            Pattern pattern = Pattern.compile(ipRegex);
            Matcher matcher = pattern.matcher(sb.toString());

            return (matcher.find()) ? matcher.group(2) : "-1";

        } catch (IOException ioe) {
            throw new IOException("Error while retreiving public ip address !");

        } finally {
            if (bf != null)
                bf.close();
        }
    }

    // _________________________________________________________________________

    /**
     * @param aLocale
     *            The {@link Locale} to use.
     * @return A {@link Map} containing all ISO country codes as keys and
     *         all countries names as values.
     */
    public static Map<String, String> getCountries(Locale aLocale) {

        Map<String, String> countries = new HashMap<String, String>();
        String[] allISOCountries = Locale.getISOCountries();
        String lang = aLocale.getLanguage();
        for (String country : allISOCountries) {
            String name = new Locale(lang, country).getDisplayCountry(aLocale);
            countries.put(country, name);
        }
        return countries;
    }

    // _________________________________________________________________________

    /**
     * Closes a list of <tt>Closeable</tt> objects quietly.<br>
     * The specified list may contain objects with null <tt>null</tt> values.
     * 
     * @param closeables - The list of <tt>Closeable</tt> object to close.
     * @see #closeStreamQuietly(Closeable)
     */
    public static void closeAllStreamsQuietly(Closeable... closeables) {
        for (Closeable aCloseable : closeables) {
            closeStreamQuietly(aCloseable);
        }
    }

    /**
     * Close quietly an object which implements the {@link Closeable} interface
     * such as <tt>InputStream</tt>, <tt>OutputStream</tt>, <tt>Reader</tt> ...
     * 
     * @param closeable - The stream to close, if <tt>null</tt> nothing is done.
     * @see #closeAllStreamsQuietly(Closeable...)
     */
    public static void closeStreamQuietly(Closeable closeable) {
        try {
            if (closeable != null)
                closeable.close();
        } catch (IOException ioe) {
            // ioe.printStackTrace(System.err);
        }
    }

    /**
     * Copies an <tt>InputStream</tt> to an <tt>OutputStream</tt> using a
     * {@link ProgressListener} if not <tt>null</tt>.
     * <p>
     * <b>NOTE</b>: The streams are not closed here.
     * </p>
     * 
     * @param in - the <code>InputStream</code> to read from
     * @param out - the <code>OutputStream</code> to write to
     * @param progressListener - The listener to use if not {@code null}.
     * @throws IOException
     */
    public static void copyStream(
            final InputStream in,
            final OutputStream out,
            final ProgressListener progressListener) throws IOException {

        if (in == null) {
            throw new IllegalArgumentException("The intpustream may not be null.");
        }
        if (out == null) {
            throw new IllegalArgumentException("The outputstream may not be null.");
        }

        if (progressListener == null) {
            IOUtils.copy(in, out);
        } else {
            final int buffer = 4096;
            final byte[] data = new byte[buffer];
            int bytesRead = 0;
            BasicProgressEvent event = new BasicProgressEvent(in.available(), bytesRead);
            while ((bytesRead = in.read(data, 0, buffer)) > 0) {
                out.write(data, 0, bytesRead);
                event.add(bytesRead);
                progressListener.onProgress(event);
            }
        }
    }
}
