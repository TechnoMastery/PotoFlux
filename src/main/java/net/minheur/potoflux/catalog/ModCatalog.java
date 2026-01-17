package net.minheur.potoflux.catalog;

import net.minheur.potoflux.PotoFlux;

import java.util.AbstractMap;
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
        if (!isPublished) return false;

        for (ModVersion version : versions.values())
            if (version.isCompatible()) return true;
        return false;
    }
    public boolean isCompatible(String version) {
        if (!isPublished) return false;

        for (Map.Entry<String, ModVersion> entry : versions.entrySet()) {
            if (entry.getKey().equals(version))
                return entry.getValue().isCompatible();
        }
        return false;
    }
    public Map.Entry<String, ModVersion> getLastestCompatibleVersion() {
        List<String> compatibleVersions = new ArrayList<>();
        for (Map.Entry<String, ModVersion> v : versions.entrySet())
            if (isCompatible(v.getKey()) || v.getValue().isPublished)
                compatibleVersions.add(v.getKey());

        String lastest = compatibleVersions.stream()
                .max(CatalogHelpers::compareVersions)
                .orElse(null);
        return new AbstractMap.SimpleEntry<>(lastest, versions.get(lastest));
    }

    public boolean isLastestCompatibleVersion(String modVersion) {
        if (!isCompatible(modVersion)) return false;

        return getLastestCompatibleVersion().getKey().equals(modVersion);
    }

    @Deprecated
    public String modId() {
        return modId;
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
            if (!isPublished) return false;
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
