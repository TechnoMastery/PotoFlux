package net.minheur.potoflux.catalog;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import net.minheur.potoflux.utils.Json;
import net.minheur.potoflux.utils.OnlineReader;
import net.minheur.potoflux.logger.LogCategories;
import net.minheur.potoflux.logger.PtfLogger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Tab for the catalog
 */
public class CatalogGetterHandler {
    /**
     * Reference to the GSON
     */
    private static final Gson GSON = Json.GSON;
    /**
     * Link to the root of the catalog
     */
    private static final String baseDir = "https://technomastery.github.io/PotoFluxAppData/modCatalog/";

    /**
     * List of all mod catalogs
     */
    private static final List<ModCatalog> catalog = new ArrayList<>();

    /**
     * Builder for filling the list if catalog
     */
    public static void buildCatalog() {
        catalog.clear();

        String mainCatalogPath = baseDir + "mainCatalog.json";

        String mainCatalogContent = getMainCatalogContent(mainCatalogPath);
        if (mainCatalogContent == null) return;

        List<String> allMods = new ArrayList<>();

        fillModIds(mainCatalogContent, allMods);

        loopThroughMods(allMods);
    }

    /**
     * From the content of the main catalog, fills the list with mod IDs.
     * @param mainCatalogContent the content of the main catalog
     * @param allMods the list to add IDs to
     */
    private static void fillModIds(String mainCatalogContent, List<String> allMods) {
        JsonArray mainCatalogJson = getMainCatalogArray(mainCatalogContent);
        mainCatalogJson.forEach(j -> allMods.add(j.getAsString()));
    }

    /**
     * Gets the {@link JsonArray} contained in the main catalog
     * @param mainCatalogContent the {@link String} content of the main catalog
     * @return the array of the main catalog
     */
    private static JsonArray getMainCatalogArray(String mainCatalogContent) {
        return GSON.fromJson(mainCatalogContent, JsonArray.class);
    }

    /**
     * For all modIDs, checks if valid then adds it the the catalog
     * @param allMods
     */
    private static void loopThroughMods(List<String> allMods) {
        for (String modId : allMods) {
            String modCatalogPath = getModCatalogPath(modId);

            String modCatalogContent = OnlineReader.read(modCatalogPath);
            if (checkContent(modId, modCatalogContent)) continue;

            ModCatalog modCatalog = GSON.fromJson(modCatalogContent, ModCatalog.class);

            if (!modCatalog.isCorrect()) catalog.add(modCatalog);
        }
    }

    /**
     * Checks if the content of a catalog is correct
     * @param modId the ID of the mod owning the catalog
     * @param modCatalogContent the content of the catalog to check
     * @return if the catalog is incorect
     */
    private static boolean checkContent(String modId, String modCatalogContent) {
        if (modCatalogContent == null) {
            PtfLogger.error("Failed to read catalog for '" + modId + "' ! Skipping...", LogCategories.CATALOG);
            return true;
        }
        else return false;
    }

    /**
     * Gets the URL to a mod's catalog
     * @param modId the mod to get the catalog
     * @return the mod's catalog URL
     */
    @Nonnull
    private static String getModCatalogPath(String modId) {
        String modCatalogPath = baseDir + modId + "/" + modId + ".json";
        return modCatalogPath;
    }

    /**
     * Gets the content of the main catalog, from its URL
     * @param mainCatalogPath the URL to the main catalog
     * @return the content of the main catalog
     */
    @Nullable
    private static String getMainCatalogContent(String mainCatalogPath) {
        String mainCatalogContent = OnlineReader.read(mainCatalogPath);
        if (mainCatalogContent == null)
            PtfLogger.error("Failed to read main catalog !", LogCategories.CATALOG);
        return mainCatalogContent;
    }

    /**
     * Gets the mod catalog list
     * @return the mod catalog list
     */
    public static List<ModCatalog> getCatalog() {
        return List.copyOf(catalog);
    }
    /**
     * Gets a mod catalog via its id
     * @param id modId of the catalog to get
     * @return the mod catalog of the given ID
     */
    public static ModCatalog getById(String id) {
        for (ModCatalog c : catalog)
            if (c.modId.equals(id)) return c;

        PtfLogger.error("Getting unexisting mod catalog: " + id, LogCategories.CATALOG);
        return null;
    }
    /**
     * Checks if a modId is known to the catalog
     * @param modId mod to check if known
     * @return if the mod is known
     */
    public static boolean isModKnown(String modId) {
        for (ModCatalog mod : catalog)
            if (mod.modId.equals(modId)) return true;
        return false;
    }
}
