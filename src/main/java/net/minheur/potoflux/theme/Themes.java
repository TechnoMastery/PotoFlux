package net.minheur.potoflux.theme;

import net.minheur.potoflux.settings.types.IComboSetting;
import net.minheur.potoflux.translations.Translations;

public enum Themes implements IComboSetting {
    DEFAULT("potoflux:theme.default", "default");

    private final String translatableName;
    private final String returnValue;

    Themes(String translatableName, String returnValue) {
        this.translatableName = translatableName;
        this.returnValue = returnValue;
    }

    @Override
    public String returnValue() {
        return returnValue;
    }

    @Override
    public String toString() {
        return Translations.get(translatableName);
    }
}
