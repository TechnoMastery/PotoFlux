package net.minheur.potoflux.screen.tabs;

import net.minheur.potoflux.registry.IRegistryType;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;

public record Tab(ResourceLocation id, String name, Class<? extends BaseTab> tabClass) implements IRegistryType {

    public BaseTab createInstance() {
        try {
            return tabClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
