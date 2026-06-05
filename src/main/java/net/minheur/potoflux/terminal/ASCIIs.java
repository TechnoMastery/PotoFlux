package net.minheur.potoflux.terminal;

import net.minheur.potoflux.settings.types.IComboSetting;
import net.minheur.potoflux.translations.Translations;

/**
 * Enum with all ASCIIs.<br>
 * Used in the settings
 */
public enum ASCIIs implements IComboSetting {
    /**
     * Basic ASCII, in plain letters
     */
    BASIC("basic", "potoflux:ascii.basic"),
    /**
     * Big ASCII, made from other chars on multi-line
     */
    BIG("big", "potoflux:ascii.big"),
    /**
     * Chiseled ASCII, enormous with many lines
     */
    CHISELED("chiseled", "potoflux:ascii.chiseled");

    /**
     * Name of the file that contains the ASCII.<br>
     * Doesn't contain the file extension, which is always {@code .txt}
     */
    private final String fileName;
    /**
     * Translation key for the ASCII's name
     */
    private final String translatableDisplayName;

    /**
     * Makes an ASCII
     * @param fileName to find the ASCII. Should lead to an existing file
     * @param translatableDisplayName for the ASCII. Do not add the {@link Translations#get(String)}.
     */
    ASCIIs(String fileName, String translatableDisplayName) {
        this.fileName = fileName;
        this.translatableDisplayName = translatableDisplayName;
    }

    /**
     * Gets the file name for the ASCII
     * @return {@link #fileName}
     */
    @Override
    public String returnValue() {
        return fileName;
    }

    /**
     * Change the display name to the translated one
     * @return {@link Translations#get} of {@linkplain #translatableDisplayName}
     */
    @Override
    public String toString() {
        return Translations.get(translatableDisplayName);
    }
}
