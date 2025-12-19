package net.minheur.potoflux.utils.logger;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public final class PtfLogger {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static String buildPrefix(List<String> data) {
        LocalTime now = LocalTime.now();
        String time = now.format(formatter);

        StringBuilder sb = new StringBuilder();
        sb.append("[").append(time).append("]");

        for (String s : data) sb.append("[").append(s).append("]");
        return sb.toString();
    }
    public static String buildPrefix(String... data) {
        if (data.length < 1) return "";
        return buildPrefix(data);
    }

    public static String buildWithCategory(String category, String message, List<String> more) {
        List<String> data = new ArrayList<>();
        data.add(category);
        data.addAll(more);

        return buildPrefix(data) + ": " + message;
    }

    // actual methods
    public static void info(String message, String... extraCategories) {
        System.out.println(buildWithCategory("INFO", message, List.of(extraCategories)));
    }
    public static void info(String message, ILogCategory category, String... extraCategories) {
        List<String> allCategories = new ArrayList<>();

        allCategories.add(category.code());
        allCategories.addAll(List.of(extraCategories));

        System.out.println(buildWithCategory("INFO", message, allCategories));
    }

    public static void warning(String message, String... extraCategories) {
        System.err.println(buildWithCategory("WARNING", message, List.of(extraCategories)));
    }
    public static void warning(String message, ILogCategory category, String... extraCategories) {
        List<String> allCategories = new ArrayList<>();

        allCategories.add(category.code());
        allCategories.addAll(List.of(extraCategories));

        System.err.println(buildWithCategory("WARNING", message, allCategories));
    }

    public static void error(String message, String... extraCategories) {
        System.err.println(buildWithCategory("ERROR", message, List.of(extraCategories)));
    }
    public static void error(String message, ILogCategory category, String... extraCategories) {
        List<String> allCategories = new ArrayList<>();

        allCategories.add(category.code());
        allCategories.addAll(List.of(extraCategories));

        System.err.println(buildWithCategory("ERROR", message, allCategories));
    }
}
