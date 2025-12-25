package net.minheur.potoflux.catalog;

import java.util.List;
import java.util.Map;

public class ModCatalog {
    public String modId;
    public boolean isPublished;
    public Map<String, ModVersion> versions;

    public static class ModVersion {
        public String fileName;
        public boolean isPublished;
        public List<String> ptfVersions;
    }
}
