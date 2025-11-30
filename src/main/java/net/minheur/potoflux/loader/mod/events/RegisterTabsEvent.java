package net.minheur.potoflux.loader.mod.events;

import net.minheur.potoflux.screen.tabs.BaseTab;
import net.minheur.potoflux.screen.tabs.TabRegistry;
import net.minheur.potoflux.utils.ResourceLocation;

public class RegisterTabsEvent {
    public void register(ResourceLocation id, String name, Class<? extends BaseTab> clazz) {
        TabRegistry.add(id, name, clazz);
    }
}
