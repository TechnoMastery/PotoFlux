package net.minheur.potoflux.loader.mod.events;

import net.minheur.potoflux.theme.ThemesRegistry;

public class RegisterThemesEvent implements IEvent {
    public final ThemesRegistry reg = new ThemesRegistry();

    @Override
    public void close() {
        reg.close();
    }
}
