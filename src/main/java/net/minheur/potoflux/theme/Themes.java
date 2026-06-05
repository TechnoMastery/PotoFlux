package net.minheur.potoflux.theme;

import net.minheur.potoflux.settings.types.IComboSetting;
import net.minheur.potoflux.translations.Translations;

/**
 * List of all themes that can be chosen from in the settings
 */
public enum Themes implements IComboSetting {
    /**
     * The default theme, chosen by the devs
     */
    DEFAULT("potoflux:theme.default", "default");

    /**
     * Display name, as a translation key
     */
    private final String translatableName;
    /**
     * Value that get stored in the prefs
     */
    private final String returnValue;

    /**
     * Makes a theme
     * @param translatableName key of translation for the display name. Do not add {@link Translations#get(String)}
     * @param returnValue value that gets stored in the prefs
     */
    Themes(String translatableName, String returnValue) {
        this.translatableName = translatableName;
        this.returnValue = returnValue;
    }

    /**
     * Getter for the {@linkplain #returnValue}
     * @return {@link #returnValue}
     */
    @Override
    public String returnValue() {
        return returnValue;
    }

    /**
     * Makes sure to display the {@linkplain #translatableName}, after translation
     * @return {@link Translations#get} with {@link #translatableName}
     */
    @Override
    public String toString() {
        return Translations.get(translatableName);
    }
}
