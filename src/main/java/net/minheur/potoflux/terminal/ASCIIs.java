package net.minheur.potoflux.terminal;

import net.minheur.potoflux.settings.types.IComboSetting;
import net.minheur.potoflux.translations.Translations;

public enum ASCIIs implements IComboSetting {
    BASIC("basic", "potoflux:ascii.basic"),
    BIG("big", "potoflux:ascii.big"),
    CHISELED("chiseled", "potoflux:ascii.chiseled");

    private final String fileName;
    private final String translatableDisplayName;

    ASCIIs(String fileName, String translatableDisplayName) {
        this.fileName = fileName;
        this.translatableDisplayName = translatableDisplayName;
    }

    @Override
    public String returnValue() {
        return fileName;
    }

    @Override
    public String toString() {
        return Translations.get(translatableDisplayName);
    }
}
