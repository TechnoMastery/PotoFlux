package net.minheur.potoflux.catalog.mods;

import net.minheur.potoflux.registry.IRegistry;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Registry for making catalog tabs
 */
public class CatalogTabRegistry implements IRegistry<ModCatalogTab> {
    /**
     * Registration for all the catalog tabs
     */
    private static final Map<ResourceLocation, ModCatalogTab> REGISTRY = new LinkedHashMap<>();

    /**
     * Gets all the catalog tabs
     * @return the collection if catalog tab
     */
    public static Collection<ModCatalogTab> getAll() {
        return REGISTRY.values();
    }

    /**
     * Adds an item to the catalog
     * @param item tab to add to the registry
     * @return the tab added
     */
    @Override
    public ModCatalogTab add(ModCatalogTab item) {
        if (REGISTRY.containsKey(item.id())) throw new IllegalArgumentException("This tab is already added: " + item.id());
        return REGISTRY.put(item.id(), item);
    }
}
