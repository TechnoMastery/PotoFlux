package net.minheur.potoflux.boot;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class BootModMetadata {
    public String modId;
    public String version;
    public String className;
    public String compatibleVersionUrl = "NONE";
    public List<String> compatibleVersions = new ArrayList<>();
    public List<String> dependenciesIds = new ArrayList<>();
    public List<String> externalDependencies = new ArrayList<>();
    public List<String> mixinConfigs = new ArrayList<>();
    public String sourcePath;
    public boolean loadable;
    public String disabledReason;

    Path sourcePath() {
        return sourcePath == null ? null : Path.of(sourcePath);
    }
}
