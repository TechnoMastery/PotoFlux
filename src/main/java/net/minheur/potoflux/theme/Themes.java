package net.minheur.potoflux.theme;

import atlantafx.base.theme.*;
import net.minheur.potoflux.settings.types.IComboSetting;
import net.minheur.potoflux.translations.Translations;

/**
 * List of all themes that can be chosen from in the settings
 */
public enum Themes implements IComboSetting {
    /**
     * The default theme, chosen by the devs
     */
    DRACULA("potoflux:theme.dracula", "dracula", new Dracula()),
    CUPERTINO_DARK("potoflux:theme.cupertino.dark", "cuper_dark", new CupertinoDark()),
    CUPERTINO_LIGHT("potoflux:theme.cupertino.light", "cuper_light", new CupertinoLight()),
    NORD_DARK("potoflux:theme.nord.dark", "nord_dark", new NordDark()),
    NORD_LIGHT("potoflux:theme.nord.light", "nord_light", new NordLight()),
    PRIMER_DARK("potoflux:theme.primer.dark", "nord_dark", new PrimerDark()),
    PRIMER_LIGHT("potoflux:theme.primer.light", "nord_light", new PrimerLight());

    /**
     * Display name, as a translation key
     */
    private final String translatableName;
    /**
     * Value that get stored in the prefs
     */
    private final String returnValue;
    private final Theme theme;

    /**
     * Makes a theme
     *
     * @param translatableName key of translation for the display name. Do not add {@link Translations#get(String)}
     * @param returnValue      value that gets stored in the prefs
     * @param theme            Actual AtlantaFX theme
     */
    Themes(String translatableName, String returnValue, Theme theme) {
        this.translatableName = translatableName;
        this.returnValue = returnValue;
        this.theme = theme;
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
    public Theme getTheme() {
        return theme;
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
