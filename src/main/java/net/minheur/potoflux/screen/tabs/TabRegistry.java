package net.minheur.potoflux.screen.tabs;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class TabRegistry {
    private static final Map<String, TabType> REGISTRY = new LinkedHashMap<>();

    public static void register(TabType tab) {
        REGISTRY.put(tab.getId(), tab);
    }

    public static Collection<TabType> getAll() {
        return REGISTRY.values();
    }
}

