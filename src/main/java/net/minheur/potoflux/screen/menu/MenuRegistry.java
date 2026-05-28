package net.minheur.potoflux.screen.menu;

import net.minheur.potoflux.registry.IRegistry;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class MenuRegistry implements IRegistry<PotoMenuItem> {

    private static final Map<ResourceLocation, PotoMenuItem> REGISTRY = new LinkedHashMap<>();
    @Contract(pure = true)
    public static @NotNull Collection<PotoMenuItem> getAll() {
        return REGISTRY.values();
    }

    @Override
    public PotoMenuItem add(@NotNull PotoMenuItem item) {
        if (REGISTRY.containsKey(item.id())) throw new IllegalArgumentException("This tab is already added: " + item.id());
        return REGISTRY.put(item.id(), item);
    }
}
