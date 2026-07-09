package net.minheur.potoflux.theme;

import net.minheur.potoflux.settings.Settings;
import net.minheur.potoflux.settings.types.IComboSetting;
import net.minheur.potoflux.translations.Translations;

/**
 * List of all themes that can be chosen from in the settings
 */
public enum Themes implements IComboSetting {
    /**
     * The default theme, chosen by the devs
     */
    DRACULA("potoflux:theme.dracula", "dracula", "styles/atlanta/dracula.css"),
    CUPERTINO_DARK("potoflux:theme.cupertino.dark", "cuper_dark", "styles/atlanta/cupertino-dark.css"),
    CUPERTINO_LIGHT("potoflux:theme.cupertino.light", "cuper_light", "styles/atlanta/cupertino-light.css"),
    NORD_DARK("potoflux:theme.nord.dark", "nord_dark", "styles/atlanta/nord-dark.css"),
    NORD_LIGHT("potoflux:theme.nord.light", "nord_light", "styles/atlanta/nord-light.css"),
    PRIMER_DARK("potoflux:theme.primer.dark", "nord_dark", "styles/atlanta/primer-dark.css"),
    PRIMER_LIGHT("potoflux:theme.primer.light", "nord_light", "styles/atlanta/primer-light.css");

    /**
     * Display name, as a translation key
     */
    private final String translatableName;
    /**
     * Value that get stored in the prefs
     */
    private final String returnValue;
    private final String sheetDir;

    /**
     * Makes a theme
     *
     * @param translatableName key of translation for the display name. Do not add {@link Translations#get(String)}
     * @param returnValue      value that gets stored in the prefs
     * @param sheetDir
     */
    Themes(String translatableName, String returnValue, String sheetDir) {
        this.translatableName = translatableName;
        this.returnValue = returnValue;
        this.sheetDir = sheetDir;
    }

    /**
     * Getter for the {@linkplain #returnValue}
     *
     * @return {@link #returnValue}
     */
    @Override
    public String returnValue() {
        return returnValue;
    }
    public String getSheetDir() {
        return sheetDir;
    }

    public static Themes getFromKey(String key) {
        for (Themes t : Themes.values())
            if (t.returnValue.equals(key)) return t;
        return DRACULA;
    }

    /**
     * Makes sure to display the {@linkplain #translatableName}, after translation
     *
     * @return {@link Translations#get} with {@link #translatableName}
     */
    @Override
    public String toString() {
        return Translations.get(translatableName);
    }
}
