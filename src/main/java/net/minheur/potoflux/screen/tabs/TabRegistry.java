package net.minheur.potoflux.screen.tabs;

import net.minheur.potoflux.utils.ResourceLocation;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class TabRegistry {
    private static final Map<ResourceLocation, Tab> REGISTRY = new LinkedHashMap<>();

    public static Collection<Tab> getAll() {
        return REGISTRY.values();
    }

    public static Tab add(Tab tab) {
        if (REGISTRY.containsKey(tab.id())) throw new IllegalArgumentException("This tab is already added !");
        return REGISTRY.put(tab.id(), tab);
    }
    public static Tab add(ResourceLocation id, String name, Class<? extends BaseTab> tabClass) {
        Tab t = new Tab(id, name, tabClass);
        return add(t);
    }
}

