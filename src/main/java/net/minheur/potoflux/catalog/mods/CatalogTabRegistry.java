package net.minheur.potoflux.catalog.mods;

import net.minheur.potoflux.registry.IRegistry;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class CatalogTabRegistry implements IRegistry<ModCatalogTab> {
    public static final CatalogTabRegistry INSTANCE = new CatalogTabRegistry();

    private static final Map<ResourceLocation, ModCatalogTab> REGISTRY = new LinkedHashMap<>();

    public static Collection<ModCatalogTab> getAll() {
        return REGISTRY.values();
    }

    @Override
    public ModCatalogTab add(ModCatalogTab item) {
        if (REGISTRY.containsKey(item.id())) throw new IllegalArgumentException("This tab is already added: " + item.id());
        return REGISTRY.put(item.id(), item);
    }
}
