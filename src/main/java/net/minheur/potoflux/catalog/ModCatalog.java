package net.minheur.potoflux.catalog;

import net.minheur.potoflux.PotoFlux;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModCatalog {
    public String modId;
    public boolean isPublished;
    public Map<String, ModVersion> versions;

    public boolean isCorrect() {
        if (modId == null || modId.trim().isEmpty()) return false;
        if (versions == null) return false;
        for (Map.Entry<String, ModVersion> entry : versions.entrySet()) {
            if (entry.getKey() == null || entry.getKey().trim().isEmpty()) return false;
            if (!entry.getValue().isCorrect()) return false;
        }
        return true;
    }

    public boolean isCompatible() {
        for (ModVersion version : versions.values())
            if (version.isCompatible()) return true;
        return false;
    }
    private boolean isCompatible(String version) {
        for (Map.Entry<String, ModVersion> entry : versions.entrySet()) {
            if (entry.getKey().equals(version))
                return entry.getValue().isCompatible();
        }
        return false;
    }
    public ModVersion getLastestCompatibleVersion() {
        List<String> compatibleVersions = new ArrayList<>();
        for (String v : versions.keySet())
            if (isCompatible(v)) compatibleVersions.add(v);

        String lastest = compatibleVersions.stream()
                .max(CatalogHelpers::compareVersions)
                .orElse(null);

        return versions.get(lastest);
    }

    public static class ModVersion {
        public String fileName;
        public boolean isPublished;
        public List<String> ptfVersions;

        public boolean isCorrect() {
            if (fileName == null || fileName.trim().isEmpty()) return false;
            for (String v : ptfVersions) if (v== null || v.trim().isEmpty()) return false;
            return true;
        }

        public boolean isCompatible() {
            return ptfVersions.contains(PotoFlux.getVersion());
        }

        public String getPtfVersions() {
            StringBuilder sb = new StringBuilder();
            for (String s : ptfVersions) {
                sb.append(s);
                if (ptfVersions.indexOf(s) != ptfVersions.size() -1) sb.append(", ");
            }
            return sb.toString();
        }
    }
}
