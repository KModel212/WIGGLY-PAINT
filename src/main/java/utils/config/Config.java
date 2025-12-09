package utils.config;

import java.util.Properties;
import java.io.InputStream;

public class Config {

    // ============================================================
    // Properties Container
    // ============================================================
    private static final Properties props = new Properties();


    // ============================================================
    // Load configuration files on startup
    // ============================================================
    static {
        load("/Properties/application.properties");
        load("/Properties/theme.properties");
    }


    // ============================================================
    // Load a .properties file into the Properties map
    // ============================================================
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
    public static String getString(String key) {
        return props.getProperty(key);
    }

    public static double getDouble(String key) {
        return Double.parseDouble(props.getProperty(key));
    }

    public static int getInt(String key) {
        return Integer.parseInt(props.getProperty(key));
    }
}
