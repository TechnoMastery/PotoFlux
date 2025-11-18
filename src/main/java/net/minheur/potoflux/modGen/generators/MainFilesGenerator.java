package net.minheur.potoflux.modGen.generators;

import net.minheur.potoflux.modGen.GeneratorStageHandler;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class MainFilesGenerator {
    private final File output;

    public MainFilesGenerator(File output) {
        this.output = output;
    }

    public void mkGradlew() {
        try {
            copyResourceToOutput("modGen/mainFiles/gradlew.txt", "gradlew", true);
            copyResourceToOutput("modGen/mainFiles/gradlewBat.txt", "gradlew.bat", false);
        } catch (IOException e) {
            e.printStackTrace();
            GeneratorStageHandler.showGenerationError();
        }
    }
    public void mkSettingsGradle() {
        try {
            copyResourceToOutput("modGen/mainFiles/settingsGradle.txt", "settings.gradle", false);
        } catch (IOException e) {
            e.printStackTrace();
            GeneratorStageHandler.showGenerationError();
        }
    }
    public void mkGradleWrapper() {
        try {
            File wrapperDir = new File(output, "gradle/wrapper");

            copyResourcesDirectory("modGen/mainFiles/gradle/wrapper", wrapperDir);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            GeneratorStageHandler.showGenerationError();
        }
    }

    private void copyResourceToOutput(String resourcePath, String outputFileName, boolean executable) throws IOException {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IllegalStateException("Resource " + resourcePath + " not found!");
            }

            File outFile = new File(output, outputFileName);

            // Copy data
            try (OutputStream out = new FileOutputStream(outFile)) {
                in.transferTo(out);
            }

            // Optional exec rights
            if (executable) {
                outFile.setExecutable(true);
            }
        }
    }
    private void copyResourcesDirectory(String resourcePath, File targetDir) throws IOException, URISyntaxException {
        ClassLoader cl = getClass().getClassLoader();
        var url = cl.getResource(resourcePath);

        if (url == null) {
            throw new IllegalStateException("Resource folder not found: " + resourcePath);
        }

        var uri = url.toURI();
        var path = Paths.get(uri);

        if (!Files.exists(path)) {
            throw new IllegalStateException("Resource directory does not exist: " + path);
        }

        Files.walk(path).forEach(p -> {
            try {
                var relative = path.relativize(p).toString();
                File out = new File(targetDir, relative);

                if (Files.isDirectory(p)) {
                    out.mkdirs();
                } else {
                    try (InputStream in = cl.getResourceAsStream(resourcePath + "/" + relative)) {
                        if (in == null) throw new IOException("Cannot read resource: " + resourcePath + "/" + relative);

                        out.getParentFile().mkdirs();
                        Files.copy(in, out.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
