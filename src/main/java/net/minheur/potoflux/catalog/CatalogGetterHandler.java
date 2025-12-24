package net.minheur.potoflux.catalog;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import net.minheur.potoflux.utils.Json;
import net.minheur.potoflux.utils.OnlineReader;
import net.minheur.potoflux.utils.logger.LogCategories;
import net.minheur.potoflux.utils.logger.PtfLogger;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CatalogGetterHandler {
    private static final Gson GSON = Json.GSON;
    private static final String baseDir = "https://technomastery.github.io/PotoFluxAppData/modCatalog/";

    private static final Map<String, ModCatalog> catalog = new HashMap<>();

    public static void buildCatalog() {
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

            Type typeToken = new TypeToken<ModCatalog>() {}.getType();
            ModCatalog modCatalog = GSON.fromJson(modCatalogContent, typeToken);

            catalog.put(modId, modCatalog);
        }
    }

    public static ModCatalog getCatalog(String modId) {
        return catalog.get(modId);
    }
}
