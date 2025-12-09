package utils.themes;

import java.util.ArrayList;
import java.util.List;

public class ThemeManager {

    // -------------------------------------------------
    // CURRENT THEME (default = classic)
    // -------------------------------------------------
    private static Theme currentTheme = new Theme("pastel");

    // List of UI listeners that want to update when theme changes
    private static final List<Runnable> listeners = new ArrayList<>();


    // -------------------------------------------------
    // GET CURRENT THEME
    // -------------------------------------------------
    public static Theme get() {
        return currentTheme;
    }


    // -------------------------------------------------
    // CHANGE THEME (classic, pastel, night, etc.)
    // -------------------------------------------------
    public static void setTheme(String name) {
        try {
            currentTheme = new Theme(name);
        } catch (RuntimeException e) {
            System.err.println("[ThemeManager] Missing theme config for: " + name);
            return;
        }

        // 🔔 Notify all listeners (UI refresh)
        for (Runnable r : listeners) r.run();
    }


    // -------------------------------------------------
    // UI components register here to get updates
    // -------------------------------------------------
    public static void addListener(Runnable uiRefresher) {
        listeners.add(uiRefresher);
    }
}
