package net.minheur.potoflux.modGen.generators;

import net.minheur.potoflux.modGen.GeneratorStageHandler;
import net.minheur.potoflux.modGen.data.ModData;
import net.minheur.potoflux.modGen.data.ModDependency;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static net.minheur.potoflux.DownloadsHandler.*;

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
            GeneratorStageHandler.showGenerationError(e);
        }
    }
    public void mkSettingsGradle() {
        try {
            downloadToFile(modGenURL + "settings.gradle", new File(output, "settings.gradle"));
        } catch (IOException e) {
            GeneratorStageHandler.showGenerationError(e);
        }
    }
    public void mkGradleWrapper() {
        try {

            downloadToFile(modGenURL + "gradleWrapper/gradle-wrapper.jar",
                    new File(output, "gradle/wrapper/gradle-wrapper.jar"));
            downloadToFile(modGenURL + "gradleWrapper/gradle-wrapper.properties",
                    new File(output, "gradle/wrapper/gradle-wrapper.properties"));

        } catch (IOException e) {
            GeneratorStageHandler.showGenerationError(e);
        }
    }
    public void mkBuildGradle() {
        try {
            downloadToFile(modGenURL + "build.gradle",
                    new File(output, "build.gradle"));
        } catch (IOException e) {
            GeneratorStageHandler.showGenerationError(e);
        }
    }
    public void mkGradleProperties(String modId, String modPackage, ModData data) {
        try {
            String content = getOnlineFileContent(modGenURL + "gradle.properties").formatted(
                    "mod_id=" + modId,
                    "mod_name=" + data.getModName(),
                    "mod_license=" + data.getModLicence(),
                    "mod_version=" + data.getModInitialVersion(),
                    "mod_group_id=" + modPackage,
                    data.getModAuthor().trim().isEmpty() ? "#mod_authors=Your name" : "mod_authors=" + data.getModAuthor(),
                    data.getModCredits().trim().isEmpty() ? "#mod_credits=Your name" : "mod_credits=" + data.getModCredits(),
                    "mod_description=" + data.getModDesc()
            );

            File file = new File(output, "grade.properties");
            file.getParentFile().mkdirs();

            Files.writeString(file.toPath(), content,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            GeneratorStageHandler.showGenerationError(e);
        }
    }
    public void mkTomlMcmeta(ModData data, List<ModDependency> dependencies) {
        try {
            String modTomlContent = getOnlineFileContent(modGenURL + "mods.toml").formatted(
                    data.getIssueURL().trim().isEmpty() ? "#issueTrackerURL=your issue URL #optional" : "issueTrackerURL=" + data.getIssueURL() + " #optinal",
                    data.getUpdateJSONURL().trim().isEmpty() ? "#updateJSONURL=your json URL #optional" : "updateJSONURL=" + data.getUpdateJSONURL() + " #optional",
                    data.getDisplayURL().trim().isEmpty() ? "#displayURL=your display URL #optional" : "displayURL=" + data.getDisplayURL() + " #optional",
                    buildDependenciesToml(dependencies)
            );
            String mcmetaContent = getOnlineFileContent(modGenURL + "pack.mcmeta").formatted(
                    data.getModDesc()
            );

            File modsToml = new File(output, "src/main/resources/META-INF/mods/toml");
            File packMcmeta = new File(output, "src/main/resources/pack.mcmeta");
            modsToml.getParentFile().mkdirs();
            packMcmeta.mkdirs();

            Files.writeString(modsToml.toPath(), modTomlContent,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
            Files.writeString(packMcmeta.toPath(), mcmetaContent,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            GeneratorStageHandler.showGenerationError(e);
        }
    }

    private String buildDependenciesToml(List<ModDependency> dependencies) {
        StringBuilder builder = new StringBuilder();

        for (ModDependency dep : dependencies) {
            String subBuilder = "[[dependencies.${modid}]]\n" +
                    "modId=\"" + dep.modId() + "\"\n" +
                    "mandatory=" + dep.mandatory() + "\n" +
                    "versionRange=\"" + dep.versionRange() + "\"\n" +
                    "ordering=\"" + dep.ordering() + "\"\n" +
                    "side=\"" + dep.side() + "\"\n";

            builder.append(subBuilder);
        }

        return builder.toString();

    }
}
