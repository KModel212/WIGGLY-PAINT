package utils.themes;

public class ThemeManager {

    private static Theme currentTheme = new Theme("pastel");


    public static void setTheme(String name) {
        currentTheme = new Theme(name);
    }

    public static Theme get() {
        return currentTheme;
    }
}
