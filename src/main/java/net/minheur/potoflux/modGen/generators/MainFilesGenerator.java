package net.minheur.potoflux.modGen.generators;

import net.minheur.potoflux.modGen.GeneratorStageHandler;
import net.minheur.potoflux.modGen.data.ModData;

import java.io.*;
import java.nio.file.Path;

import static net.minheur.potoflux.DownloadsHandler.*;
import static net.minheur.potoflux.PotoFlux.getProgramDir;

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

    public void mkBuildGradle() {
        try {
            downloadToFile(modGenURL + "build.gradle",
                    new File(output, "build.gradle"));
        } catch (IOException e) {
            e.printStackTrace();
            GeneratorStageHandler.showGenerationError();
        }
    }

    public void mkGradleProperties(String modId, String modPackage, ModData data) {
        try {
            String content = getOnlineFileContent(modGenURL + "gradle.properties");
            content = content.replace("<modid>", "mod_id=" + modId);
            content = content.replace("<name>", "mod_name=" + data.getModName());
            content = content.replace("<license>", "mod_license=" + data.getModLicence());
            content = content.replace("<version>", "mod_version=" + data.getModInitialVersion());
            content = content.replace("<package>", "mod_group_id=" + modPackage);
            content = content.replace("<authors>", data.getModAuthor().trim().isEmpty() ? "#mod_authors=Your name" : "mod_authors=" + data.getModAuthor());
            content = content.replace("<credits>", data.getModCredits().trim().isEmpty() ? "#mod_credits=Your name" : "mod_credits=" + data.getModCredits());
            content = content.replace("<desc>", "mod_description=" + data.getModDesc());
        } catch (IOException e) {
            e.printStackTrace();
            GeneratorStageHandler.showGenerationError();
        }
    }
}
