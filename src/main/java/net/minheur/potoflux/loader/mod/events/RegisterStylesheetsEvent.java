package net.minheur.potoflux.loader.mod.events;

import net.minheur.potoflux.styles.StylesheetsRegistry;

public class RegisterStylesheetsEvent implements IEvent {
    public final StylesheetsRegistry reg = new StylesheetsRegistry();

    @Override
    public void close() {
        reg.close();
    }
}
