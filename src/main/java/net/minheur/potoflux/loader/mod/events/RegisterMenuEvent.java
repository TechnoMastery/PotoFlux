package net.minheur.potoflux.loader.mod.events;

import net.minheur.potoflux.screen.menu.MenuRegistry;

/**
 * Register menu event class.
 */
public class RegisterMenuEvent implements IEvent {
    /**
     * The reg field.
     */
    public final MenuRegistry reg = new MenuRegistry();

    @Override
    public void close() {
        reg.close();
    }
}
