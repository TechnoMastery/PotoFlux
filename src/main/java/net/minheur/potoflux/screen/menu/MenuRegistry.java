package net.minheur.potoflux.screen.menu;

import net.minheur.potoflux.registry.AbstractRegistry;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Registry of all menues
 */
public class MenuRegistry extends AbstractRegistry<PotoMenuItem> {

    /**
     * Actual reg containing all items
     */
    private static final Map<ResourceLocation, PotoMenuItem> REGISTRY = new LinkedHashMap<>();
    /**
     * Gets all items of the reg
     * @return {@link #REGISTRY}
     */
    @Contract(pure = true)
    public static @NotNull Collection<PotoMenuItem> getAll() {
        return REGISTRY.values();
    }

    /**
     * Adds an item to the reg
     * @param item object to add to the reg
     * @return the added items
     */
    @Override
    public PotoMenuItem add(@NotNull PotoMenuItem item) {
        if (REGISTRY.containsKey(item.id())) throw new IllegalArgumentException("This tab is already added: " + item.id());
        return REGISTRY.put(item.id(), item);
    }
}
