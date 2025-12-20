package net.minheur.potoflux.loader.mod.events;

import net.minheur.potoflux.translations.AbstractTranslationsRegistry;
import net.minheur.potoflux.translations.Translations;

/**
 * Send your main class to search for your langs.<br>
 * {@code registerMod(YourModMainClass.class)}
 */
public class RegisterLangEvent {
    public void registerLang(AbstractTranslationsRegistry registry) {
        Translations.registerTranslations(registry);
    }
}
