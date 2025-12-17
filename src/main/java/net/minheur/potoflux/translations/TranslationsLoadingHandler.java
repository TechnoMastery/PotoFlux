package net.minheur.potoflux.translations;

import java.util.HashMap;
import java.util.Map;

public class TranslationsLoadingHandler {
    private final String modId;
    private final Map<Lang, Map<String, String>> modTranslations = new HashMap<>();

    public TranslationsLoadingHandler(String modId) {
        this.modId = modId;
    }

    public class TranslationBuilder {
        private final String key;

        public TranslationBuilder(String key) {
            this.key = key;
        }

        public TranslationBuilder lang(Lang lang, String value) {
            Map<String, String> translations = modTranslations.get(lang);
            if (translations.put(key, value) != null) throw new IllegalArgumentException("Duplicate translation key for key '" + key + "', in mod '" + modId + "'");
            return this;
        }
    }
}
