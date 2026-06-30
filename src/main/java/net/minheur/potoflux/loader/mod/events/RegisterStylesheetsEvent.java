package net.minheur.potoflux.loader.mod.events;

import net.minheur.potoflux.styles.StylesheetsRegistry;

/**
 * Register stylesheets event class.
 */
public class RegisterStylesheetsEvent implements IEvent {
    /**
     * The reg field.
     */
    public final StylesheetsRegistry reg = new StylesheetsRegistry();

    @Override
    public void close() {
        reg.close();
    }
}
