package net.minheur.potoflux.settings;

import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.logger.PtfLogger;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class OptionalFeaturesManager {

    private static final Properties features = new Properties();
    private static boolean hasLoaded = false;

    private OptionalFeaturesManager() {}

    /**
     * Called to load optional features from the file
     */
    public static void load() {
        if (hasLoaded) return;
        hasLoaded = true;

        Path featuresPath = PotoFlux.getProgramDir().resolve("optionalFeatures.properties");

        if (Files.notExists(featuresPath))
            createOptionalFeatures(featuresPath);

        else try (InputStream in = Files.newInputStream(featuresPath)) {

            features.load(in);

        } catch (IOException e) {
            e.printStackTrace();
            PtfLogger.error("Could not get optionalFeatures.properties !");
        }

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

    private static Object getRaw(String key, Object defaultValue) {
        return features.getOrDefault(key, defaultValue);
    }
    private static Object getRaw(String key) {
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
