package net.minheur.potoflux.translations;

import net.minheur.potoflux.utils.PtfLogger;

import java.util.HashMap;
import java.util.Map;

public class Translations {
    private static Lang loadedLang = Lang.EN;
    private static final Map<Lang, Map<String, String>> allTranslations = new HashMap<>();
    static {
        for (Lang lang : Lang.values()) allTranslations.put(lang, new HashMap<>());
    }

    // registry
    public static void registerTranslations(AbstractTranslationsRegistry registry) {
        registry.register();
        Map<Lang, Map<String, String>> registryTranslations = registry.getTranslations();

        for (Map.Entry<Lang, Map<String, String>> entry : registryTranslations.entrySet()) {

            Map<String, String> langTranslations = allTranslations.get(entry.getKey());
            if (langTranslations == null) throw new IllegalStateException("Translations missing lang !");

            langTranslations.putAll(entry.getValue());
        }
    }

    // loading
    /**
     * Allow to change loaded lang
     * @param lang the lang you want to load
     * @return if the loaded lang has changed
     */
    public static boolean load(Lang lang) {
        if (loadedLang == lang) {
            PtfLogger.warning("loaded lang is already loaded: " + lang.code, "translations");
            return false;
        }
        loadedLang = lang;
        PtfLogger.info("loaded lang: " + lang.code, "translations");
        return true;
    }

    // getter
    public static String get(String key) {
        Map<String, String> tr = allTranslations.get(loadedLang);
        if (tr == null) throw new IllegalStateException("Translations missing lang !");
        String t = tr.get(key);
        if (t == null) {
            PtfLogger.error("no translations set for queried '" + key + "'", "translations");
            return key;
        }
        return t;
    }

}
