package net.minheur.potoflux.settings;

import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.logger.PtfLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Manages the optional features
 */
public final class OptionalFeaturesManager {

    /**
     * Props of the features, gotten from the file
     */
    private static final Properties features = new Properties();
    /**
     * Map of all features by id and value as {@link OptionalFeature}
     */
    private static final Map<String, OptionalFeature> featureMap = new LinkedHashMap<>();

    /**
     * Locks the class's instanciation
     */
    private OptionalFeaturesManager() {}

    /**
     * Called to load optional features from the file
     */
    public static void load() {
        features.clear();

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
    private static void createOptionalFeatures(@NotNull Path featuresPath) {
        try {
            Files.createDirectories(featuresPath.getParent());
            Files.createFile(featuresPath);

        } catch (IOException e) {
            e.printStackTrace();
            PtfLogger.error("Could not create optionalFeatures.properties !");
        }
    }

    /**
     * Fills the {@linkplain #featureMap} with data from the {@linkplain #features}
     */
    private static void fillMap() {
        featureMap.clear();

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

    /**
     * Getter for the {@linkplain #featureMap}
     * @return {@link #featureMap}
     */
    public static Map<String, OptionalFeature> getFeatureMap() {
        return featureMap;
    }

    /**
     * Gets a raw feature
     * @param key used in the file
     * @param defaultValue used if the feature is not set
     * @return the value of the feature
     */
    public static Object getRaw(String key, Object defaultValue) {
        return features.getOrDefault(key, defaultValue);
    }
    /**
     * Gets a raw feature
     * @param key used in the file
     * @return the value of the feature, {@code null} if none set
     */
    public static Object getRaw(String key) {
        return getRaw(key, null);
    }

    /**
     * Gets feature with a {@link String} value
     * @param key used in the file
     * @param defaultValue used if the feature is not set
     * @return the value of the feature
     */
    public static String getString(String key, String defaultValue) {
        return features.getProperty(key, defaultValue);
    }
    /**
     * Gets a feature with a {@link String} value
     * @param key used in the file
     * @return the value of the feature, {@code null} if none set
     */
    public static @Nullable String getString(String key) {
        return getString(key, null);
    }

    /**
     * Gets a feature with a {@link Boolean} value
     * @param key used in the file
     * @param defaultValue used if the feature is not set
     * @return the value of the feature
     */
    public static Boolean getBoolean(String key, Boolean defaultValue) {
        Object o = getRaw(key, defaultValue);
        if (o == null) return defaultValue;
        if (o instanceof String s)
            return Boolean.parseBoolean(s);
        else return defaultValue;
    }
    /**
     * Gets a feature with a {@link Boolean} value
     * @param key used in the file
     * @return the value of the feature, {@code null} if none set
     */
    public static @Nullable Boolean getBoolean(String key) {
        return getBoolean(key, null);
    }

    /**
     * Gets a feature with an {@link Integer} value
     * @param key used in the file
     * @param defaultValue used if the feature is not set
     * @return the value of the feature
     */
    public static Integer getInt(String key, Integer defaultValue) {
        Object o = getRaw(key, defaultValue);
        if (o == null) return defaultValue;
        if (o instanceof String s)
            return Integer.parseInt(s);
        else return defaultValue;
    }
    /**
     * Gets a feature with a {@link Integer} value
     * @param key used in the file
     * @return the value of the feature, {@code null} if none set
     */
    public static Integer getInt(String key) {
        return getInt(key, null);
    }

}
