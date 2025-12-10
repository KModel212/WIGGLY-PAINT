package utils.themes;

import java.util.ArrayList;
import java.util.List;

/**
 * Global theme manager for WigglyPaint.
 * <p>
 * The {@code ThemeManager} stores the currently active {@link Theme},
 * allows switching to a different theme, and notifies all registered
 * listeners whenever a theme change occurs.
 *
 * <h3>Responsibilities:</h3>
 * <ul>
 *     <li>Hold the active {@link Theme}</li>
 *     <li>Load new themes on request</li>
 *     <li>Notify UI components so they can refresh themselves</li>
 * </ul>
 *
 * <p>Typical usage:
 * <pre>
 * ThemeManager.setTheme("classic");  // switch to classic theme
 * Theme t = ThemeManager.get();      // retrieve current theme
 *
 * ThemeManager.addListener(() -> {
 *     // refresh UI colors
 * });
 * </pre>
 *
 * UI components such as {@code MenuPane}, {@code CanvasPane},
 * {@code BrushPane}, and rendering utilities register listeners
 * so their appearance automatically updates when themes change.
 */
public class ThemeManager {

    // ============================================================
    // Current theme (default = classic)
    // ============================================================

    /** The theme currently in use across the entire application. */
    private static Theme currentTheme = new Theme("classic");

    /** Listeners notified whenever the theme changes. */
    private static final List<Runnable> listeners = new ArrayList<>();


    // ============================================================
    // Get Current Theme
    // ============================================================

    /**
     * Retrieves the theme currently active in the application.
     *
     * @return the active {@link Theme}
     */
    public static Theme get() {
        return currentTheme;
    }


    // ============================================================
    // Set Theme
    // ============================================================

    /**
     * Changes the active theme by name.
     * <p>
     * Attempts to construct a new {@link Theme} using the provided name,
     * loading corresponding values from <code>theme.properties</code>.
     * <p>
     * If the theme name is invalid or missing in the config, the change
     * is ignored, and an error message is printed.
     *
     * <p>After successfully loading a new theme, all registered listeners
     * are executed in order to refresh UI colors.
     *
     * @param name theme name prefix (e.g., "classic", "pastel", "night")
     */
    public static void setTheme(String name) {
        try {
            currentTheme = new Theme(name);
        } catch (RuntimeException e) {
            System.err.println("[ThemeManager] Missing theme config for: " + name);
            return;
        }

        // Notify listeners (UI refresh)
        for (Runnable r : listeners) {
            r.run();
        }
    }


    // ============================================================
    // Register Listener
    // ============================================================

    /**
     * Registers a callback to be executed whenever the theme changes.
     * <p>
     * UI components should use this to update colors, redraw graphics,
     * or rebuild icons in response to theme changes.
     *
     * @param uiRefresher callback executed on theme update
     */
    public static void addListener(Runnable uiRefresher) {
        listeners.add(uiRefresher);
    }
}
