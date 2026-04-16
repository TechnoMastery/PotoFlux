package net.minheur.potoflux.screen.menu;

import net.minheur.potoflux.registry.IRegistry;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class MenuRegistry implements IRegistry<PotoMenuItem> {

    private static final Map<ResourceLocation, PotoMenuItem> REGISTRY = new LinkedHashMap<>();
    public static Collection<PotoMenuItem> getAll() {
        return REGISTRY.values();
    }

    @Override
    public PotoMenuItem add(PotoMenuItem item) {
        if (REGISTRY.containsKey(item.id())) throw new IllegalArgumentException("This tab is already added: " + item.id());
        return REGISTRY.put(item.id(), item);
    }
}
