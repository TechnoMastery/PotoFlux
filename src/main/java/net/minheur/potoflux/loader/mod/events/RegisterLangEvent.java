package net.minheur.potoflux.loader.mod.events;

import net.minheur.potoflux.translations.Lang;
import net.minheur.potoflux.translations.Translations;

import java.io.InputStream;

/**
 * Send your main class to search for your langs.<br>
 * {@code registerMod(YourModMainClass.class)}<br>
 * Place your json files in {@code resources/lang/langcode.json}
 */
public class RegisterLangEvent {
    public void registerSupportedLang(Class<?> modClass) {
        for (Lang lang : Lang.values()) {
            InputStream in = modClass.getResourceAsStream("/lang/" + lang.code + ".json");
            if (in != null) Translations.registerByStream(lang, in);
        }
    }
}
