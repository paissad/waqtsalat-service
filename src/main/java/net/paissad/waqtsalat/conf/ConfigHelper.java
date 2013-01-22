package net.paissad.waqtsalat.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import net.paissad.waqtsalat.util.CommonUtils;

public class ConfigHelper {

    private static final Properties confProps;
    private static boolean          initialized;

    private static final Object     lock = new Object();

    static {
        confProps = new Properties();
    }

    private ConfigHelper() {
    }

    public static void initialize(final File configFile) throws IOException {

        InputStream in = null;
        try {
            in = new FileInputStream(configFile);
            initialized = false;
            synchronized (lock) {
                confProps.load(in);
            }
            initialized = true;

        } finally {
            CommonUtils.closeAllStreamsQuietly(in);
        }
    }

    private static boolean isInitialized() {
        return initialized;
    }

    public static String getValue(final String key) {
        return getValue(key, null);
    }

    /**
     * @param key
     * @param mandatory
     * @return The value of the property.
     * @throws IllegalStateException If <tt>mandatory</tt> and the property's value is <tt>null</tt>.
     */
    public static String getValue(final String key, final boolean mandatory) throws IllegalStateException {
        final String value = getValue(key, null);
        if ((value == null) && mandatory) {
            throw new IllegalStateException("Value is null while it is mandatory for key : '" + key + "'");
        }
        return value;
    }

    public static String getValue(final String key, final String defaultValue) {
        if (!isInitialized()) throw new IllegalStateException("The configuration is not yet initialized !");
        synchronized (lock) {
            return confProps.getProperty(key, defaultValue);
        }
    }

}
