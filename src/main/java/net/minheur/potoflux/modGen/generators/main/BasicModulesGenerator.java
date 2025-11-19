package net.minheur.potoflux.modGen.generators.main;

import net.minheur.potoflux.modGen.GeneratorStageHandler;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import static net.minheur.potoflux.DownloadsHandler.getOnlineFileContent;
import static net.minheur.potoflux.DownloadsHandler.modGenURL;

public class BasicModulesGenerator {
    private final File srcOutput;
    private final String modPackage;
    private final String mainClassName;

    public BasicModulesGenerator(File srcOutput, String modPackage, String mainClassName) {
        this.srcOutput = srcOutput;
        this.modPackage = modPackage;
        this.mainClassName = mainClassName;
    }

    public void mkAdvancement() {
        try {
            String content = getOnlineFileContent(modGenURL + "mainModules/Advancements.java").formatted(
                    this.modPackage,
                    "import " + this.modPackage + "." + this.mainClassName + ";\n", // import main class
                    this.mainClassName, this.mainClassName
            );

            File file = new File(srcOutput, "advancement/ModAdvancements.java");
            file.getParentFile().mkdirs();

            Files.writeString(file.toPath(), content,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            GeneratorStageHandler.showGenerationError(e);
        }
    }
    public void mkBlocks() {
        try {

        }
    }
}
