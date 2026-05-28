package net.minheur.potoflux.settings;

import net.minheur.potoflux.registry.IRegistry;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class SettingRegistry implements IRegistry<Setting> {
    private static final Map<ResourceLocation, Setting> REGISTRY = new LinkedHashMap<>();

    @Contract(pure = true)
    public static @NotNull Collection<Setting> getAll() {
        return REGISTRY.values();
    }

    @Override
    public Setting add(@NotNull Setting item) {
        if (REGISTRY.containsKey(item.id())) throw new IllegalArgumentException("This setting is already added: " + item.id());
        return REGISTRY.put(item.id(), item);
    }
}
