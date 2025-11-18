package net.minheur.potoflux.modGen.generators;

import net.minheur.potoflux.modGen.GeneratorStageHandler;

import java.io.*;

import static net.minheur.potoflux.DownloadsHandler.downloadToFile;
import static net.minheur.potoflux.DownloadsHandler.modGenURL;

public class MainFilesGenerator {
    private final File output;

    public MainFilesGenerator(File output) {
        this.output = output;
    }

    public void mkGradlew() {
        try {
            downloadToFile(modGenURL + "gradlew", new File(output, "gradlew"));
            downloadToFile(modGenURL + "gradlew.bat", new File(output, "gradlew.bat"));

            new File(output, "gradlew").setExecutable(true);
        } catch (IOException e) {
            e.printStackTrace();
            GeneratorStageHandler.showGenerationError();
        }
    }
    public void mkSettingsGradle() {
        try {
            downloadToFile(modGenURL + "settings.gradle", new File(output, "settings.gradle"));
        } catch (IOException e) {
            e.printStackTrace();
            GeneratorStageHandler.showGenerationError();
        }
    }
    public void mkGradleWrapper() {
        try {

            downloadToFile(modGenURL + "gradleWrapper/gradle-wrapper.jar",
                    new File(output, "gradle/wrapper/gradle-wrapper.jar"));
            downloadToFile(modGenURL + "gradleWrapper/gradle-wrapper.properties",
                    new File(output, "gradle/wrapper/gradle-wrapper.properties"));

        } catch (IOException e) {
            e.printStackTrace();
            GeneratorStageHandler.showGenerationError();
        }
    }
}
