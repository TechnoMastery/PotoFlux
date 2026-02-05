package net.minheur.potoflux.translations;

import net.minheur.potoflux.logger.LogCategories;
import net.minheur.potoflux.logger.PtfLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * the class that handle translations
 */
public class Translations {
    /**
     * The loaded translations
     */
    private static Lang loadedLang = Lang.EN;
    /**
     * A map of all registered translations.<br>
     * The main map is the lang one. For each lang, we have a map of keys and translations
     */
    private static final Map<Lang, Map<String, String>> allTranslations = new HashMap<>();
    static {
        for (Lang lang : Lang.values()) allTranslations.put(lang, new HashMap<>());
    }

    // registry
    /**
     * Adds a translation registry to {@link #allTranslations}
     * @param registry the reg to add translations from
     */
    public static void registerTranslations(AbstractTranslationsRegistry registry) {
        registry.registerTranslations();
        Map<Lang, Map<String, String>> registryTranslations = registry.getTranslations();

        for (Map.Entry<Lang, Map<String, String>> entry : registryTranslations.entrySet()) {

            Map<String, String> langTranslations = allTranslations.get(entry.getKey());
            if (langTranslations == null) throw new IllegalStateException("Translations missing lang !");

            langTranslations.putAll(entry.getValue());
        }
    }

    // loading
    /**
     * Allow to change {@link #loadedLang}
     * @param lang the lang you want to load
     * @return if the loaded lang has changed
     */
    public static boolean load(Lang lang) {
        if (loadedLang == lang) {
            PtfLogger.warning("loaded lang is already loaded: " + lang.code, LogCategories.TRANSLATIONS);
            return false;
        }
        loadedLang = lang;
        PtfLogger.info("loaded lang: " + lang.code, LogCategories.TRANSLATIONS);
        return true;
    }

    /**
     * Allow to change {@link #loadedLang}, by a lang code ({@link String})
     * @param langCode the code of the lang you want to load
     * @return if the loaded lang has changed
     */
    public static boolean load(String langCode) {
        for (Lang l : Lang.values()) {
            if (l.code.equals(langCode)) return load(l);
        }
        throw new IllegalArgumentException("Trying to load unsupported lang !");
    }

    // getter
    /**
     * Get a translation in the {@link #loadedLang} from its key
     * @param key the key of the translation to get
     * @return the translation of the key in the {@link #loadedLang}
     */
    public static String get(String key) {
        Map<String, String> tr = allTranslations.get(loadedLang);
        if (tr == null) throw new IllegalStateException("Translations missing lang !");
        String t = tr.get(key);
        if (t == null) {
            if (loadedLang != Lang.EN) {
                Map<String, String> enLang = allTranslations.get(Lang.EN);
                String enT = enLang.get(key);
                if (enT != null) {
                    PtfLogger.warning("No translations set for '" + key + "' in lang " + loadedLang.code,
                            LogCategories.TRANSLATIONS);
                    return enT;
                }
            }

            PtfLogger.error("No translations set for queried '" + key + "'", LogCategories.TRANSLATIONS);
            return key;
        } else return t;
    }

}
