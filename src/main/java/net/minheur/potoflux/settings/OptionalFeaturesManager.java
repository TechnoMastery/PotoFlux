package net.minheur.potoflux.settings;

import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.logger.PtfLogger;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public final class OptionalFeaturesManager {

    private static final Properties features = new Properties();
    private static final Map<String, OptionalFeature> featureMap = new LinkedHashMap<>();

    private OptionalFeaturesManager() {}

    /**
     * Called to load optional features from the file
     */
    public static void load() {

        Path featuresPath = PotoFlux.getProgramDir().resolve("optionalFeatures.properties");

        if (Files.notExists(featuresPath))
            createOptionalFeatures(featuresPath);

        else try (InputStream in = Files.newInputStream(featuresPath)) {

            features.load(in);

        } catch (IOException e) {
            e.printStackTrace();
            PtfLogger.error("Could not get optionalFeatures.properties !");
            return;
        }

        fillMap();

    }
    /**
     * Creates the {@code optionalFeatures.properties} file
     * @param featuresPath the path to create the file to
     */
    private static void createOptionalFeatures(Path featuresPath) {
        try {
            Files.createDirectories(featuresPath.getParent());
            Files.createFile(featuresPath);

        } catch (IOException e) {
            e.printStackTrace();
            PtfLogger.error("Could not create optionalFeatures.properties !");
        }
    }

    private static void fillMap() {

        for (Map.Entry<Object, Object> entry : features.entrySet()) {

            // ignore non-string keys
            if (!(entry.getKey() instanceof String key)) continue;

            Object rawValue = entry.getValue();

            // ignore non-string values
            if (!(rawValue instanceof String value)) continue;

            // boolean case
            if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {

                boolean boolValue = Boolean.parseBoolean(value);
                featureMap.put(key, new OptionalFeature(boolValue));
                continue;

            }

            // int case
            try {
                int intValue = Integer.parseInt(value);
                featureMap.put(key, new OptionalFeature(intValue));
                continue;
            } catch (NumberFormatException ignored) {}

            // string callback
            featureMap.put(key, new OptionalFeature(value));

        }

    }

    public static Map<String, OptionalFeature> getFeatureMap() {
        return featureMap;
    }

    public static Object getRaw(String key, Object defaultValue) {
        return features.getOrDefault(key, defaultValue);
    }
    public static Object getRaw(String key) {
        return getRaw(key, null);
    }

    public static String getString(String key, String defaultValue) {
        return features.getProperty(key, defaultValue);
    }
    public static @Nullable String getString(String key) {
        return getString(key, null);
    }

    public static Boolean getBoolean(String key, Boolean defaultValue) {
        Object o = getRaw(key, defaultValue);
        if (o instanceof Boolean b)
            return b;
        else return defaultValue;
    }
    public static @Nullable Boolean getBoolean(String key) {
        return getBoolean(key, null);
    }

    public static Integer getInt(String key, Integer defaultValue) {
        Object o = getRaw(key, defaultValue);
        if (o instanceof Integer i)
            return i;
        else return defaultValue;
    }
    public static Integer getInt(String key) {
        return getInt(key, null);
    }

}
