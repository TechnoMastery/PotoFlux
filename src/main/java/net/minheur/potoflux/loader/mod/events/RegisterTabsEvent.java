package net.minheur.potoflux.loader.mod.events;

import net.minheur.potoflux.catalog.mods.CatalogTabRegistry;
import net.minheur.potoflux.screen.tabs.TabRegistry;

public class RegisterTabsEvent {
    public final TabRegistry reg = new TabRegistry();
    public final CatalogTabRegistry catalogReg = new CatalogTabRegistry();
}
