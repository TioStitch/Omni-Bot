package org.tiostitch.omnibot.utilities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Color {

    RESET("\u001B[0m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    AQUA("\u001B[36m"),
    BLUE("\u001B[34m"),
    GOLDEN("\u001B[33m"),
    YELLOW("\u001B[33m"),
    FULL_YELLOW("\u001B[43m"),
    DARK_GRAY("\u001B[37m"),
    BLACK("\u001B[40m");

    private final String code;

    public static String getCodeByRGB(String R, String G, String B) {
        return "\u001B[38;2;" + R + ";" + G + ";" + B + "m";
    }
}
