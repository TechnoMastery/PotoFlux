package net.minheur.potoflux.translations;

/**
 * This contains all languages supported by potoflux.
 */
public enum Lang {
    /**
     * French translations
     */
    FR("fr"),
    /**
     * English translations
     */
    EN("en");

    /**
     * The code of the translations.<br>
     * Used to get from the user pref
     */
    public final String code;

    /**
     * Registers a lang.
     * @param langCode the {@link #code} of the lang
     */
    Lang(String langCode) {
        this.code = langCode;
    }
}
