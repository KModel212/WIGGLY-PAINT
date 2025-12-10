package utils.config;

import java.util.Properties;
import java.io.InputStream;

/**
 * Simple configuration loader for the WigglyPaint application.
 * <p>
 * This utility class loads and stores values from standard Java
 * <code>.properties</code> files. It provides typed getters for
 * accessing configuration values as <code>String</code>,
 * <code>int</code>, or <code>double</code>.
 *
 * <p>On startup, two configuration files are loaded automatically:
 * <ul>
 *     <li><b>/Properties/application.properties</b></li>
 *     <li><b>/Properties/theme.properties</b></li>
 * </ul>
 *
 * These files must exist on the classpath. Missing files cause the
 * application to throw a runtime error.
 *
 * <h3>Examples</h3>
 * <pre>
 * int w = Config.getInt("application.default_width");
 * double r = Config.getDouble("theme.dot.radius");
 * String name = Config.getString("theme.name");
 * </pre>
 */
public class Config {

    // ============================================================
    // Properties Container
    // ============================================================

    /** Shared properties map storing all values loaded from config files. */
    private static final Properties props = new Properties();


    // ============================================================
    // Static Initializer — load files on class load
    // ============================================================

    static {
        load("/Properties/application.properties");
        load("/Properties/theme.properties");
    }


    // ============================================================
    // Load a .properties file into the Properties map
    // ============================================================

    /**
     * Loads a <code>.properties</code> file into the internal properties map.
     * If the file cannot be found, a {@link RuntimeException} is thrown.
     *
     * @param fileName resource path to the properties file
     */
    private static void load(String fileName) {
        try (InputStream in = Config.class.getResourceAsStream(fileName)) {
            if (in == null) {
                throw new RuntimeException("Cannot find " + fileName);
            }
            props.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ============================================================
    // Accessors
    // ============================================================

    /**
     * Retrieves a raw string from the configuration.
     *
     * @param key property key
     * @return the string associated with the key
     */
    public static String getString(String key) {
        return props.getProperty(key);
    }

    /**
     * Retrieves a configuration value as a double.
     * The property must be parsable via {@link Double#parseDouble(String)}.
     *
     * @param key property key
     * @return double value stored under the key
     * @throws NumberFormatException if the value is not numeric
     */
    public static double getDouble(String key) {
        return Double.parseDouble(props.getProperty(key));
    }

    /**
     * Retrieves a configuration value as an integer.
     * The property must be parsable via {@link Integer#parseInt(String)}.
     *
     * @param key property key
     * @return int value stored under the key
     * @throws NumberFormatException if the value is not numeric
     */
    public static int getInt(String key) {
        return Integer.parseInt(props.getProperty(key));
    }
}
