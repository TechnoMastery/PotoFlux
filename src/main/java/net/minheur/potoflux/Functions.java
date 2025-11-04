package net.minheur.potoflux;

import javax.swing.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Functions {
    public static void exit(int delay, int status) {
        Timer exitDelay = new Timer(delay, ev -> PotoFlux.runProgramClosing(status));
        exitDelay.setRepeats(false);
        exitDelay.start();
    }

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static void logInfo(String info) {
        LocalTime now = LocalTime.now();
        String time = now.format(formatter);
        System.out.println("[" + time + "] " + info);
    }
    public static void logError(String error) {
        LocalTime now = LocalTime.now();
        String time = now.format(formatter);
        System.err.println("[" + time + "] " + error);
    }

    public static String removeProhibitedChar(String s) {
        if (s == null) return null;
        return s.replaceAll("[^\\p{L}\\p{N} @!.?:;*$+/→\\-^¨()#§]+", "");
    }
    public static String escapeHtml(String s) {
        if (s == null) return null;
        return s.replace("&", "")
                .replace("<", "")
                .replace(">", "");
    }
}
