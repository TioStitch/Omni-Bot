package org.tiostitch.omnibot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.tiostitch.omnibot.utilities.Color;

public class OmniLogger {

    @Getter @AllArgsConstructor
    public enum Type {
        SUCCESS(Color.GREEN.getCode() + "[+] "),
        WARNING(Color.GOLDEN.getCode() + "[!] "),
        CRITICAL(Color.RED.getCode() + "[-] ");

        private final String ping;
    }

    public static void warn(String message) {
        System.out.println("\n" + Type.WARNING.getPing() + message + "\n");
    }

    public static void critical(String message) {
        System.out.println("\n\n" + Type.CRITICAL.getPing() + message + "\n\n");
    }

    public static void success(String message) {
        System.out.println(Type.SUCCESS.getPing() + message);
    }
}
