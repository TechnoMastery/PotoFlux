package net.minheur.potoflux.settings;

import net.minheur.potoflux.registry.IRegistry;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Registry of all settings
 */
public class SettingRegistry implements IRegistry<Setting> {
    /**
     * List of all settings to add
     */
    private static final Map<ResourceLocation, Setting> REGISTRY = new LinkedHashMap<>();

    /**
     * Gets all settings
     * @return {@link #REGISTRY}
     */
    @Contract(pure = true)
    public static @NotNull Collection<Setting> getAll() {
        return REGISTRY.values();
    }

    /**
     * Adds an item to the {@linkplain #REGISTRY}
     * @param item object to add to the reg
     * @return the added item
     */
    @Override
    public Setting add(@NotNull Setting item) {
        if (REGISTRY.containsKey(item.id())) throw new IllegalArgumentException("This setting is already added: " + item.id());
        return REGISTRY.put(item.id(), item);
    }
}
