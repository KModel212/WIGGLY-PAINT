package utils.themes;

import java.util.ArrayList;
import java.util.List;

public class ThemeManager {

    // ============================================================
    // Current theme (default = pastel)
    // ============================================================
    private static Theme currentTheme = new Theme("classic");

    // UI listeners that update when theme changes
    private static final List<Runnable> listeners = new ArrayList<>();


    // ============================================================
    // Get Current Theme
    // ============================================================
    public static Theme get() {
        return currentTheme;
    }


    // ============================================================
    // Set Theme (e.g., classic, pastel, night)
    // Notifies all registered listeners.
    // ============================================================
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
    // Register a listener to be updated on theme change
    // ============================================================
    public static void addListener(Runnable uiRefresher) {
        listeners.add(uiRefresher);
    }
}
