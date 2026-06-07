package net.minheur.potoflux.loader.mod.events;

import net.minheur.potoflux.translations.AbstractTranslationsRegistry;
import net.minheur.potoflux.translations.Translations;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Event to register your lang registries.
 */
public class RegisterLangEvent implements IEvent {
    private final AtomicBoolean closed = new AtomicBoolean(false);

    /**
     * Call this to add your lang registry to the list
     * @param registry the registry to add to the list
     */
    public void registerLang(AbstractTranslationsRegistry registry) {
        if (closed.get()) throw new IllegalStateException("Can't add translation reg to closed event !");
        Translations.registerTranslations(registry);
    }

    @Override
    public void close() {
        closed.set(true);
    }
}
