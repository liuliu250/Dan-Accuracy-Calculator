package acccal;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class UiConfig {
    public final String windowTitle;
    public final String calculateButtonText;
    public final Font calculateButtonFont;
    public final Font labelFont;
    public final Font danChooserFont;

    private UiConfig(String windowTitle,
                     String calculateButtonText,
                     Font calculateButtonFont,
                     Font labelFont,
                     Font danChooserFont) {
        this.windowTitle = windowTitle;
        this.calculateButtonText = calculateButtonText;
        this.calculateButtonFont = calculateButtonFont;
        this.labelFont = labelFont;
        this.danChooserFont = danChooserFont;
    }

    public static UiConfig load() {
        Properties props = new Properties();
        Path file = Paths.get("config.ini");
        if (Files.exists(file)) {
            try (InputStream in = Files.newInputStream(file)) {
                props.load(in);
            } catch (IOException ignored) {
                // Keep defaults when config cannot be read.
            }
        }

        String windowTitle = getString(props, "window.title", "Dan Acc Calculator 2.3");
        String calculateText = getString(props, "button.calculate.text", "Calculate");

        Font calculateFont = getFont(props, "font.calculate", Font.SANS_SERIF, Font.PLAIN, 14);
        Font labelFont = getFont(props, "font.label", "Microsoft YaHei", Font.PLAIN, 15);
        Font danChooserFont = getFont(props, "font.danChooser", "Microsoft YaHei", Font.PLAIN, 12);

        return new UiConfig(windowTitle, calculateText, calculateFont, labelFont, danChooserFont);
    }

    private static String getString(Properties props, String key, String defaultValue) {
        String value = props.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        return value.trim();
    }

    private static Font getFont(Properties props, String prefix, String defaultName, int defaultStyle, int defaultSize) {
        String name = getString(props, prefix + ".name", defaultName);
        int style = parseStyle(getString(props, prefix + ".style", styleToString(defaultStyle)), defaultStyle);
        int size = parseInt(getString(props, prefix + ".size", String.valueOf(defaultSize)), defaultSize);
        return new Font(name, style, size);
    }

    private static int parseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException ignored) {
            return defaultValue;
        }
    }

    private static int parseStyle(String style, int defaultStyle) {
        String normalized = style.trim().toUpperCase();
        switch (normalized) {
            case "PLAIN":
                return Font.PLAIN;
            case "BOLD":
                return Font.BOLD;
            case "ITALIC":
                return Font.ITALIC;
            case "BOLD_ITALIC":
            case "ITALIC_BOLD":
                return Font.BOLD | Font.ITALIC;
            default:
                try {
                    int numeric = Integer.parseInt(normalized);
                    if (numeric == Font.PLAIN || numeric == Font.BOLD || numeric == Font.ITALIC
                        || numeric == (Font.BOLD | Font.ITALIC)) {
                        return numeric;
                    }
                } catch (NumberFormatException ignored) {
                    // fall through
                }
                return defaultStyle;
        }
    }

    private static String styleToString(int style) {
        if (style == Font.PLAIN) {
            return "PLAIN";
        }
        if (style == Font.BOLD) {
            return "BOLD";
        }
        if (style == Font.ITALIC) {
            return "ITALIC";
        }
        if (style == (Font.BOLD | Font.ITALIC)) {
            return "BOLD_ITALIC";
        }
        return String.valueOf(style);
    }
}

