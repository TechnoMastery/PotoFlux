package net.minheur.potoflux.loader.mod.events;

import net.minheur.potoflux.screen.tabs.TabRegistry;

/**
 * Event to register your lang tabs.
 */
public class RegisterTabsEvent {
    /**
     * Registry containing all tabs.
     */
    public final TabRegistry reg = new TabRegistry();
}
