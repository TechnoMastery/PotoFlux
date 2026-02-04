package net.minheur.potoflux.screen.tabs;

import net.minheur.potoflux.registry.IRegistry;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Registry to stores all tabs.<br>
 * All tabs should be added to it, otherwise they will not be found by the app.
 */
public class TabRegistry implements IRegistry<Tab> {
    /**
     * Actual registry for the tabs, by their ID and their tabs.
     */
    private static final Map<ResourceLocation, Tab> REGISTRY = new LinkedHashMap<>();

    /**
     * Getter for all the tabs
     * @return all the tabs
     */
    public static Collection<Tab> getAll() {
        return REGISTRY.values();
    }

    /**
     * Adds a tab to the reg
     * @param item tab to add to the reg
     * @return the added item
     */
    @Override
    public Tab add(Tab item) {
        if (REGISTRY.containsKey(item.id())) throw new IllegalArgumentException("This tab is already added: " + item.id());
        return REGISTRY.put(item.id(), item);
    }
}
