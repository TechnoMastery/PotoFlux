package net.minheur.potoflux.modGen;

import net.minheur.potoflux.modGen.data.DatagenModules;
import net.minheur.potoflux.modGen.data.MainModules;
import net.minheur.potoflux.modGen.data.ModData;
import net.minheur.potoflux.modGen.data.ModDependency;

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
            this.outputDir = dataGetter.getOutputDir();
            this.modId = dataGetter.getModId();
            this.modPackage = dataGetter.getModPackage();
            this.selectedModules = dataGetter.getSelectedModules();
            this.selectedDatagenModules = dataGetter.getSelectedDatagenModules();
            this.modDependencies = dataGetter.getModDependencies();
            this.modData = dataGetter.getModData();

        } catch (ModGenCanceledException ignored) {}
    }
}
