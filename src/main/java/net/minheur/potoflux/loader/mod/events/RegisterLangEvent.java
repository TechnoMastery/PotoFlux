package net.minheur.potoflux.loader.mod.events;

import net.minheur.potoflux.translations.AbstractTranslationsRegistry;
import net.minheur.potoflux.translations.Translations;

/**
 * Event to register your lang registries.
 */
public class RegisterLangEvent {
    /**
     * Call this to add your lang registry to the list
     * @param registry the registry to add to the list
     */
    public void registerLang(AbstractTranslationsRegistry registry) {
        Translations.registerTranslations(registry);
    }
}
