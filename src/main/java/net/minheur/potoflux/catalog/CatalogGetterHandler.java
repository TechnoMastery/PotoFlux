package net.minheur.potoflux.catalog;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minheur.potoflux.utils.Json;
import net.minheur.potoflux.utils.OnlineReader;
import net.minheur.potoflux.utils.logger.LogCategories;
import net.minheur.potoflux.utils.logger.PtfLogger;

import java.util.HashMap;
import java.util.Map;

public class CatalogGetterHandler {
    private static final Gson GSON = Json.GSON;
    private static final String baseDir = "https://technomastery.github.io/PotoFluxAppData/modCatalog/";

    private static final Map<String, JsonObject> catalog = new HashMap<>();

    public static void buildCatalog() {
        String mainCatalogPath = baseDir + "mainCatalog.json";

        String mainCatalogContent = OnlineReader.read(mainCatalogPath);
        if (mainCatalogContent == null) {
            PtfLogger.error("Failed to read main catalog !", LogCategories.CATALOG);
            return;
        }

        JsonArray mainCatalogJson = GSON.fromJson(mainCatalogContent, JsonArray.class);
    }
}
