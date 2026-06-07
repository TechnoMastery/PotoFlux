package net.minheur.potoflux.loader.mod.events;

import net.minheur.potoflux.screen.menu.MenuRegistry;

public class RegisterMenuEvent implements IEvent {
    public final MenuRegistry reg = new MenuRegistry();

    @Override
    public void close() {
        reg.close();
    }
}
