package net.minheur.potoflux.loader.mod.events;

import net.minheur.potoflux.screen.tabs.BaseTab;
import net.minheur.potoflux.screen.tabs.TabRegistry;

public class RegisterTabsEvent {
    public void register(String id, String name, Class<? extends BaseTab> clazz) {
        TabRegistry.register(id, name, clazz);
    }
}
