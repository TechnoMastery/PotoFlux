package net.minheur.potoflux.settings;

import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.logger.PtfLogger;

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

    public static Properties get() {
        return features;
    }

}
