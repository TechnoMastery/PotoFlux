package net.minheur.potoflux.translations;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract registry for a lang
 */
public abstract class AbstractTranslationsRegistry {
    /**
     * The modID of the mod creating translations
     */
    private final String modId;
    /**
     * All translations registered by the mod.<br>
     * The main map is the lang one. For each lang, we have a map of keys and translations
     */
    private final Map<Lang, Map<String, String>> modTranslations = new HashMap<>();

    /**
     * Creates a reg, from the modID
     * @param modId the {@link #modId} of the mod creating translations
     */
    public AbstractTranslationsRegistry(String modId) {
        this.modId = modId;
    }

    /**
     * Actually fill up {@link #modTranslations}.
     */
    public void registerTranslations() {
        modTranslations.clear();
        for (Lang lang : Lang.values()) modTranslations.put(lang, new HashMap<>());

        makeTranslation();
    }
    /**
     * Getter for the {@link #modTranslations}.
     * @return {@link #modTranslations}
     */
    public Map<Lang, Map<String, String>> getTranslations() {
        return modTranslations;
    }

    /**
     * Actual method to register translations.
     */
    protected abstract void makeTranslation();

    /**
     * Creates a {@link TranslationBuilder} with a raw key
     * @param key the full key of the translation
     * @return the new {@link TranslationBuilder}
     */
    protected TranslationBuilder add(String key) {
        return new TranslationBuilder(key);
    }
    /**
     * Creates a {@link TranslationBuilder} with a main key and children (separated by a {@code .})
     * @param mainKey the main key of the translation
     * @param children all other keys to add
     * @return the new {@link TranslationBuilder}
     */
    protected TranslationBuilder add(String mainKey, String... children) {
        StringBuilder builder = new StringBuilder(mainKey);
        for (String child : children) {
            builder.append(".");
            builder.append(child);
        }
        return add(builder.toString());
    }

    /**
     * Adds a translation for a tab
     * @param id the ID of the tab
     * @param children the optional children of the translation
     * @return the new {@link TranslationBuilder}
     */
    protected TranslationBuilder addTab(String id, String... children) {
        return add("tabs." + id, children);
    }
    /**
     * Adds a translation for a command usage
     * @param id the ID of the command
     * @param children the optional children of the translation
     * @return the new {@link TranslationBuilder}
     */
    protected TranslationBuilder addCommandUse(String id, String... children) {
        return add("command." + id + ".use", children);
    }
    /**
     * Adds a translation for a command
     * @param id the ID of the command
     * @param children the optional children of the translation
     * @return the new {@link TranslationBuilder}
     */
    protected TranslationBuilder addCommand(String id, String... children) {
        return add("command." + id, children);
    }

    /**
     * Adds the mod ID before a translation key
     * @param key the key to be preceded by the mod ID
     * @return the full ID
     */
    private String getKeyMod(String key) {
        return modId + ":" + key;
    }

    /**
     * Builder for translations
     */
    public class TranslationBuilder {
        /**
         * The full key for the translation
         */
        private final String key;

        /**
         * Creates the builder
         * @param key the key for the translation
         */
        public TranslationBuilder(String key) {
            this.key = getKeyMod(key);
        }

        public TranslationBuilder lang(Lang lang, String value) {
            Map<String, String> translations = modTranslations.get(lang);
            if (translations.put(key, value) != null) throw new IllegalArgumentException("Duplicate translation key '" + key + "'");
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
