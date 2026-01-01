package net.minheur.potoflux.screen.tabs;

import net.minheur.potoflux.registry.IRegistry;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class TabRegistry implements IRegistry<Tab> {
    private static final Map<ResourceLocation, Tab> REGISTRY = new LinkedHashMap<>();

    public static Collection<Tab> getAll() {
        return REGISTRY.values();
    }

    @Override
    public Tab add(Tab item) {
        if (REGISTRY.containsKey(item.id())) throw new IllegalArgumentException("This tab is already added: " + item.id());
        return REGISTRY.put(item.id(), item);
    }
}
