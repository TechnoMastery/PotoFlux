package net.minheur.potoflux.modGen;

import net.minheur.potoflux.modGen.data.DatagenModules;
import net.minheur.potoflux.modGen.data.MainModules;
import net.minheur.potoflux.modGen.data.ModData;
import net.minheur.potoflux.modGen.data.ModDependency;
import net.minheur.potoflux.modGen.generators.MainFilesGenerator;
import net.minheur.potoflux.modGen.generators.main.BasicModulesGenerator;

import javax.swing.*;
import java.io.File;
import java.util.List;
import java.util.Set;

import static net.minheur.potoflux.Functions.toClassName;

public class GeneratorStageHandler {
    // --- setup vars ---
    private final JPanel owner;
    // --- hard-needed vars ---
    private File outputDir;
    private File srcOutputDir;
    private String modId;
    private String mainClassName;
    private String modPackage;
    private String modPackageFile;
    // --- actual data ---
    private Set<MainModules> selectedModules;
    private Set<DatagenModules> selectedDatagenModules;
    private List<ModDependency> modDependencies;
    private ModData modData;

    public GeneratorStageHandler(JPanel owner) {
        this.owner = owner;
    }
    
    public static GeneratorStageHandler createGenerator(JPanel owner) {
        return new GeneratorStageHandler(owner);
    }

    public static void cancel() {
        throw new ModGenCanceledException();
    }
    public void run() {
        try {
            // --- data getter ---
            ModDataGetterHandler dataGetter = new ModDataGetterHandler(owner);
            dataGetter.run();
            this.outputDir = dataGetter.getOutputDir();
            this.modId = dataGetter.getModId();
            this.modPackage = dataGetter.getModPackage();
            this.selectedModules = dataGetter.getSelectedModules();
            this.selectedDatagenModules = dataGetter.getSelectedDatagenModules();
            this.modDependencies = dataGetter.getModDependencies();
            this.modData = dataGetter.getModData();

            this.mainClassName = toClassName(this.modId);
            this.modPackageFile = this.modPackage.replace(".", "/");
            this.srcOutputDir = new File(this.outputDir, "src/main/" + this.modPackageFile);

            // --- main file generator ---
            MainFilesGenerator mainGen = new MainFilesGenerator(this.outputDir);
            mainGen.mkGradlew();
            mainGen.mkSettingsGradle();
            mainGen.mkGradleWrapper();
            mainGen.mkBuildGradle();
            mainGen.mkGradleProperties(this.modId, this.modPackage, this.modData);
            mainGen.mkTomlMcmeta(this.modData, this.modDependencies);

            // --- main class ---

            // --- project files generator ---

            // --- generate modules ---
            BasicModulesGenerator advancementGenerator = new BasicModulesGenerator(this.srcOutputDir,
                    this.modPackage, this.mainClassName);
            if (this.selectedModules.contains(MainModules.ADVANCEMENT)) advancementGenerator.mkAdvancement();

        } catch (ModGenCanceledException ignored) {}
    }

    public static void showGenerationError(Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "ERROR while generating.\nPlease return this to the devs.",
                "ERROR", JOptionPane.ERROR_MESSAGE);
    }
}
