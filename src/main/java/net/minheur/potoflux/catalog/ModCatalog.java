package net.minheur.potoflux.catalog;

import net.minheur.potoflux.PotoFlux;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This is a mod's catalog, containing all data gotten from online
 */
public class ModCatalog {
    /**
     * ID of the online mod
     */
    public String modId;
    /**
     * If the mod is published
     */
    public boolean isPublished;
    /**
     * Map containing all registered version, linked to their name
     */
    public Map<String, ModVersion> versions;

    /**
     * Checks if the main catalog data is correct
     * @return if the catalog is correct
     */
    public boolean isCorrect() {
        if (modId == null || modId.trim().isEmpty()) return false;
        if (versions == null) return false;
        for (Map.Entry<String, ModVersion> entry : versions.entrySet()) {
            if (entry.getKey() == null || entry.getKey().trim().isEmpty()) return false;
            if (!entry.getValue().isCorrect()) return false;
        }
        return true;
    }

    /**
     * Checks if the mod is compatible with the actual potoflux version
     * @return if the mod is compatible
     */
    public boolean isCompatible() {
        if (!isPublished) return false;

        for (ModVersion version : versions.values())
            if (version.isCompatible()) return true;
        return false;
    }
    /**
     * Check if the mod is compatible with the given potoflux version
     * @param version the potoflux version to check
     * @return if the version is compatible
     */
    public boolean isCompatible(String version) {
        if (!isPublished) return false;

        for (Map.Entry<String, ModVersion> entry : versions.entrySet()) {
            if (entry.getKey().equals(version))
                return entry.getValue().isCompatible();
        }
        return false;
    }
    /**
     * Get the lastest version compatible with actual potoflux version (it's corresponding map entry)
     * @return the lastest compatible version
     */
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

    /**
     * Checks if the given version is the lastest compatible version
     * @param modVersion mod version to check
     * @return if the mdo version is the lastest compatible
     */
    public boolean isLastestCompatibleVersion(String modVersion) {
        if (!isCompatible(modVersion)) return false;

        return getLastestCompatibleVersion().getKey().equals(modVersion);
    }

    /**
     * Gets the mod ID
     * @return the mod's ID
     */
    @Deprecated
    public String modId() {
        return modId;
    }

    /**
     * Inner class to store versions
     */
    public static class ModVersion {
        /**
         * The file name for the version. Can be null if not published
         */
        public String fileName;
        /**
         * Handle if this version is published
         */
        public boolean isPublished;
        /**
         * Lists all potoflux version this mod version is compatible with
         */
        public List<String> ptfVersions;

        /**
         * Checks if the mod version main data are correct
         * @return if the version is correct
         */
        public boolean isCorrect() {
            if (fileName == null || fileName.trim().isEmpty()) return false;
            for (String v : ptfVersions) if (v== null || v.trim().isEmpty()) return false;
            return true;
        }

        /**
         * Checks if this mod version is compatible with actual potoflux version
         * @return if this version is compatible with actual potoflux
         */
        public boolean isCompatible() {
            if (!isPublished) return false;
            return ptfVersions.contains(PotoFlux.getVersion());
        }

        /**
         * Gets all compatible potoflux version of this version as a string (separated by a comma)
         * @return the list of compatible potoflux versions
         */
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
