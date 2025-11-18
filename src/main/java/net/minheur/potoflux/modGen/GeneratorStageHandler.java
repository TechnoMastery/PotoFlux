package net.minheur.potoflux.modGen;

import net.minheur.potoflux.modGen.data.DatagenModules;
import net.minheur.potoflux.modGen.data.MainModules;
import net.minheur.potoflux.modGen.data.ModData;
import net.minheur.potoflux.modGen.data.ModDependency;
import net.minheur.potoflux.modGen.generators.MainFilesGenerator;

import javax.swing.*;
import java.io.File;
import java.util.List;
import java.util.Set;

public class GeneratorStageHandler {
    // --- setup vars ---
    private final JPanel owner;
    // --- hard-needed vars ---
    private File outputDir;
    private String modId;
    private String modPackage;
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

            // --- main file generator ---
            MainFilesGenerator mainGen = new MainFilesGenerator(this.outputDir);
            mainGen.mkGradlew();
            mainGen.mkSettingsGradle();
            mainGen.mkGradleWrapper();
            mainGen.mkBuildGradle();

        } catch (ModGenCanceledException ignored) {}
    }

    public static void showGenerationError() {
        JOptionPane.showMessageDialog(null, "ERROR while generating.\nPlease return this to the devs.",
                "ERROR", JOptionPane.ERROR_MESSAGE);
    }
}
