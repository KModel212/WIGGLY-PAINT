package utils.themes;

import javafx.scene.paint.Color;
import utils.config.Config;

public class Theme {

    public final String name;

    public final Color bg;
    public final Color fg;
    public final Color primary;
    public final Color secondary;
    public final Color accent;

    public Theme(String name) {
        this.name = name;

        this.bg = loadColor("color.bg");
        this.fg = loadColor("color.fg");
        this.primary = loadColor("color.primary");
        this.secondary = loadColor("color.secondary");
        this.accent = loadColor("color.accent");
    }

    private Color loadColor(String key) {
        String fullKey = name + "." + key;
        String hex = Config.getString(fullKey);

        if (hex == null) {
            throw new RuntimeException("Missing theme key: " + fullKey);
        }

        return Color.web(hex);
    }
}
