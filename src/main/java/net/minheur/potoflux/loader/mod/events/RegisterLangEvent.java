package net.minheur.potoflux.loader.mod.events;

import net.minheur.potoflux.translations.AbstractTranslationsRegistry;
import net.minheur.potoflux.translations.Lang;
import net.minheur.potoflux.translations.Translations;
import net.minheur.potoflux.translations.TranslationsOld;

import java.io.InputStream;

/**
 * Send your main class to search for your langs.<br>
 * {@code registerMod(YourModMainClass.class)}
 */
public class RegisterLangEvent {
    public void registerSupportedLang(Class<?> modClass) {
        for (Lang lang : Lang.values()) {
            InputStream in = modClass.getResourceAsStream("/lang/" + lang.code + ".json");
            if (in != null) TranslationsOld.registerByStream(lang, in);
        }
    }
    public void registerLang(AbstractTranslationsRegistry registry) {
        Translations.registerTranslations(registry);
    }
}
