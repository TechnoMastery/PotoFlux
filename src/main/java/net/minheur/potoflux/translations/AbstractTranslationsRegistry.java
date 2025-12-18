package net.minheur.potoflux.translations;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTranslationsRegistry {
    private final String modId;
    private final Map<Lang, Map<String, String>> modTranslations = new HashMap<>();

    public AbstractTranslationsRegistry(String modId) {
        this.modId = modId;
    }

    public void register() {
        modTranslations.clear();
        for (Lang lang : Lang.values()) modTranslations.put(lang, new HashMap<>());

        makeTranslation();
    }
    public Map<Lang, Map<String, String>> getTranslations() {
        return modTranslations;
    }

    protected abstract void makeTranslation();

    protected TranslationBuilder add(String key) {
        return new TranslationBuilder(key);
    }
    protected TranslationBuilder add(String mainKey, String... children) {
        StringBuilder builder = new StringBuilder(mainKey);
        for (String child : children) {
            builder.append(".");
            builder.append(child);
        }
        return add(builder.toString());
    }

    protected TranslationBuilder addTab(String id, String... children) {
        return add("tab." + id, children);
    }
    protected TranslationBuilder addCommand(String id, String... children) {
        return add("command." + id, children);
    }

    private String getKeyMod(String key) {
        return modId + ":" + key;
    }

    public class TranslationBuilder {
        private final String key;

        public TranslationBuilder(String key) {
            this.key = key;
        }

        public TranslationBuilder lang(Lang lang, String value) {
            Map<String, String> translations = modTranslations.get(lang);
            if (translations.put(getKeyMod(key), value) != null) throw new IllegalArgumentException("Duplicate translation key '" + key + "'");
            return this;
        }

        public TranslationBuilder en(String value) {
            return lang(Lang.EN, value);
        }
        public TranslationBuilder fr(String value) {
            return lang(Lang.FR, value);
        }
    }
}
