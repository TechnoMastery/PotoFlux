package net.minheur.potoflux.catalog;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import net.minheur.potoflux.utils.Json;
import net.minheur.potoflux.utils.OnlineReader;
import net.minheur.potoflux.logger.LogCategories;
import net.minheur.potoflux.logger.PtfLogger;

import java.util.ArrayList;
import java.util.List;

public class CatalogGetterHandler {
    private static final Gson GSON = Json.GSON;
    private static final String baseDir = "https://technomastery.github.io/PotoFluxAppData/modCatalog/";

    private static final List<ModCatalog> catalog = new ArrayList<>();

    public static void buildCatalog() {
        catalog.clear();

        String mainCatalogPath = baseDir + "mainCatalog.json";

        String mainCatalogContent = OnlineReader.read(mainCatalogPath);
        if (mainCatalogContent == null) {
            PtfLogger.error("Failed to read main catalog !", LogCategories.CATALOG);
            return;
        }

        JsonArray mainCatalogJson = GSON.fromJson(mainCatalogContent, JsonArray.class);
        List<String> allMods = new ArrayList<>();

        mainCatalogJson.forEach(j -> allMods.add(j.getAsString()));

        for (String modId : allMods) {
            String modCatalogPath = baseDir + modId + "/" + modId + ".json";

            String modCatalogContent = OnlineReader.read(modCatalogPath);
            if (modCatalogContent == null) {
                PtfLogger.error("Failed to read catalog for '" + modId + "' ! Skipping...", LogCategories.CATALOG);
                continue;
            }

            ModCatalog modCatalog = GSON.fromJson(modCatalogContent, ModCatalog.class);

            if (!modCatalog.isCorrect()) catalog.add(modCatalog);
        }
    }

    public static List<ModCatalog> getCatalog() {
        return List.copyOf(catalog);
    }
    public static ModCatalog getById(String id) {
        for (ModCatalog c : catalog)
            if (c.modId.equals(id)) return c;

        PtfLogger.error("Getting unexisting mod catalog: " + id, LogCategories.CATALOG);
        return null;
    }
    public static boolean isModKnown(String modId) {
        for (ModCatalog mod : catalog)
            if (mod.modId.equals(modId)) return true;
        return false;
    }
}
