package utils.themes;

import javafx.scene.paint.Color;
import utils.config.Config;

/**
 * Represents a single named color theme for WigglyPaint.
 * <p>
 * A theme is defined by five key colors:
 * <ul>
 *     <li><b>bg</b>        — background color</li>
 *     <li><b>fg</b>        — foreground / text / UI line color</li>
 *     <li><b>primary</b>   — primary accent color</li>
 *     <li><b>secondary</b> — secondary accent color</li>
 *     <li><b>accent</b>    — highlight accent color</li>
 * </ul>
 *
 * <p>Theme definitions are loaded from <code>theme.properties</code>
 * using the following key format:
 *
 * <pre>
 * classic.color.bg        = #ffffff
 * classic.color.fg        = #000000
 * classic.color.primary   = #ff0097
 * classic.color.secondary = #0097ff
 * classic.color.accent    = #ffff00
 *
 * pastel.color.bg         = #f6f6f6
 * pastel.color.fg         = #3a3a3a
 * ...
 * </pre>
 *
 * The prefix (<code>classic</code>, <code>pastel</code>, etc.)
 * is the theme's <b>name</b>.
 *
 * <p>This class is used internally by {@link ThemeManager}, which keeps track
 * of the current theme and notifies listeners when theme changes occur.
 */
public class Theme {

    // ============================================================
    // Theme name and color fields
    // ============================================================

    /** The identifying name of the theme (e.g., "classic", "pastel"). */
    public final String name;

    /** Background color for UI and canvas. */
    public final Color bg;

    /** Foreground color (lines, text, borders). */
    public final Color fg;

    /** Primary accent color. */
    public final Color primary;

    /** Secondary accent color. */
    public final Color secondary;

    /** A bright highlight accent color. */
    public final Color accent;


    // ============================================================
    // Constructor
    // ============================================================

    /**
     * Creates a theme by loading all required color keys from the config file.
     * <p>
     * Keys are looked up using the pattern:
     * <pre>
     * name.color.bg
     * name.color.fg
     * name.color.primary
     * name.color.secondary
     * name.color.accent
     * </pre>
     *
     * @param name the theme name prefix
     * @throws RuntimeException if any required key is missing
     */
    public Theme(String name) {
        this.name = name;

        this.bg        = loadColor("color.bg");
        this.fg        = loadColor("color.fg");
        this.primary   = loadColor("color.primary");
        this.secondary = loadColor("color.secondary");
        this.accent    = loadColor("color.accent");
    }


    // ============================================================
    // Helper: retrieve a single color from Config
    // ============================================================

    /**
     * Loads a hex color from the configuration using a key derived
     * from the theme name.
     * <p>
     * Example full key:
     * <pre>
     * classic.color.fg
     * pastel.color.accent
     * </pre>
     *
     * @param key suffix of the config key
     * @return a JavaFX {@link Color} parsed from hex format
     * @throws RuntimeException if the config key is missing
     */
    private Color loadColor(String key) {
        String fullKey = name + "." + key;
        String hex = Config.getString(fullKey);

        if (hex == null) {
            throw new RuntimeException("Missing theme key: " + fullKey);
        }

        return Color.web(hex);
    }
}
