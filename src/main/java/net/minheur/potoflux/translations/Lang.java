package net.minheur.potoflux.translations;

import net.minheur.potoflux.settings.types.IComboSetting;

/**
 * This contains all languages supported by potoflux.
 */
public enum Lang implements IComboSetting {
    /**
     * English translations
     */
    EN("en", "English"),
    /**
     * French translations
     */
    FR("fr", "Francais"),
    /**
     * German translations
     */
    DE("de", "Deutsch"),
    /**
     * Espagnol translations
     */
    ES("es", "Español");

    /**
     * The code of the translations.<br>
     * Used to get from the user pref
     */
    public final String code;
    private final String name;

    /**
     * Registers a lang.
     *
     * @param langCode the {@link #code} of the lang
     * @param name     the display name of the lang
     */
    Lang(String langCode, String name) {
        this.code = langCode;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String returnValue() {
        return code;
    }
}
