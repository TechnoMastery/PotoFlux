package net.minheur.potoflux.settings;

import net.minheur.potoflux.registry.IRegistry;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SettingRegistry implements IRegistry<Setting> {
    private static final Map<ResourceLocation, Setting> REGISTRY = new HashMap<>();

    public static Collection<Setting> getAll() {
        return REGISTRY.values();
    }

    @Override
    public Setting add(Setting item) {
        if (REGISTRY.containsKey(item.id())) throw new IllegalArgumentException("This setting is already added: " + item.id());
        return REGISTRY.put(item.id(), item);
    }
}
