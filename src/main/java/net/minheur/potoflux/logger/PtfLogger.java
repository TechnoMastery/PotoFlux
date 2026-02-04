package net.minheur.potoflux.logger;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class used to log in potoflux.
 */
public final class PtfLogger {
    /**
     * Formatter used to print the time in the logs
     */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * Method to get the prefix of the command.<br>
     * It contains the time, and all the components given in args. They are put in between {@code []}
     * @param data the list of components to add to the prefix
     * @return the built prefix
     */
    public static String buildPrefix(List<String> data) {
        LocalTime now = LocalTime.now();
        String time = now.format(formatter);

        StringBuilder sb = new StringBuilder();
        sb.append("[").append(time).append("]");

        for (String s : data) sb.append("[").append(s).append("]");
        return sb.toString();
    }
    /**
     * Builder retuning {@link #buildPrefix(List)}, but with the categories as a vararg.
     * @param data vararg of the categories
     * @return the built prefix
     */
    public static String buildPrefix(String... data) {
        return buildPrefix(Arrays.stream(data).toList());
    }

    /**
     * Builds the message with a main category
     * @param category main category of the message
     * @param message content of the log
     * @param more adds more categories to the log
     * @return the message
     */
    public static String buildWithCategory(String category, String message, List<String> more) {
        List<String> data = new ArrayList<>();
        data.add(category);
        data.addAll(more);

        return buildPrefix(data) + ": " + message;
    }

    // actual methods
    /**
     * Print an info log message, with a content and optional categories
     * @param message content of the log
     * @param extraCategories optional categories of the log
     */
    public static void info(String message, String... extraCategories) {
        System.out.println(buildWithCategory("INFO", message, List.of(extraCategories)));
    }
    /**
     * Print an info log message, with a content, a main log category (in the form of an {@link ILogCategory}) and optional categories
     * @param message content of the log
     * @param category main category of the log, should be a {@link ILogCategory}
     * @param extraCategories optional categories of the log
     */
    public static void info(String message, ILogCategory category, String... extraCategories) {
        List<String> allCategories = new ArrayList<>();

        allCategories.add(category.code());
        allCategories.addAll(List.of(extraCategories));

        System.out.println(buildWithCategory("INFO", message, allCategories));
    }

    /**
     * Print a warning log message, with a content and optional categories
     * @param message content of the log
     * @param extraCategories optional categories of the log
     */
    public static void warning(String message, String... extraCategories) {
        System.err.println(buildWithCategory("WARNING", message, List.of(extraCategories)));
    }
    /**
     * Print a warning log message, with a content, a main log category (in the form of an {@link ILogCategory}) and optional categories
     * @param message content of the log
     * @param category main category of the log, should be a {@link ILogCategory}
     * @param extraCategories optional categories of the log
     */
    public static void warning(String message, ILogCategory category, String... extraCategories) {
        List<String> allCategories = new ArrayList<>();

        allCategories.add(category.code());
        allCategories.addAll(List.of(extraCategories));

        System.err.println(buildWithCategory("WARNING", message, allCategories));
    }

    /**
     * Print an error log message, with a content and optional categories
     * @param message content of the log
     * @param extraCategories optional categories of the log
     */
    public static void error(String message, String... extraCategories) {
        System.err.println(buildWithCategory("ERROR", message, List.of(extraCategories)));
    }
    /**
     * Print an error log message, with a content, a main log category (in the form of an {@link ILogCategory}) and optional categories
     * @param message content of the log
     * @param category main category of the log, should be a {@link ILogCategory}
     * @param extraCategories optional categories of the log
     */
    public static void error(String message, ILogCategory category, String... extraCategories) {
        List<String> allCategories = new ArrayList<>();

        allCategories.add(category.code());
        allCategories.addAll(List.of(extraCategories));

        System.err.println(buildWithCategory("ERROR", message, allCategories));
    }
}
