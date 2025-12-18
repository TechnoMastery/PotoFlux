package net.minheur.potoflux.utils;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public final class PtfLogger {
    // private static final DateTimeFormatter formatter

    public static String buildPrefix(List<String> data) {
        StringBuilder sb = new StringBuilder();
        for (String s : data) sb.append("[").append(s).append("]");
        return sb.toString();
    }
    public static String buildPrefix(String... data) {
        if (data.length < 1) return "";
        return buildPrefix(data);
    }

    public static String buildWithCategory(String category, String message, String... more) {
        List<String> data = new ArrayList<>();
        data.add(category);
        data.addAll(List.of(more));

        StringBuilder out = new StringBuilder(buildPrefix(data));
        out.append(": ").append(message);
        return out.toString();
    }

    public static void info(String message, String... extraCategories) {
        System.out.println(buildWithCategory("INFO", message, extraCategories));
    }
    public static void warning(String message, String... extraCategories) {
        System.err.println(buildWithCategory("WARNING", message, extraCategories));
    }
    public static void error(String message, String... extraCategories) {
        System.err.println(buildWithCategory("ERROR", message, extraCategories));
    }
}
