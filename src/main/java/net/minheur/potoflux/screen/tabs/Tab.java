package net.minheur.potoflux.screen.tabs;

import net.minheur.potoflux.registry.IRegistryType;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;

/**
 * A registry item to make tabs
 * @param id the ID of your tab
 * @param name the name of your tab (shown in the tab list)
 * @param tabClass the class of the tab, used to actually get the panel
 */
public record Tab(ResourceLocation id, String name, Class<? extends BaseTab> tabClass) implements IRegistryType {
/**
 * Creates an instance of the tab class ({@link #tabClass})
 */
    public BaseTab createInstance() {
        try {
            return tabClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
