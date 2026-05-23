package net.minheur.potoflux.settings;

import net.minheur.potoflux.settings.types.PreferencesTypes;

public class SettingInfo<T> {

    private final PreferencesTypes type;
    private T actualValue;

    public boolean modified = false;

    public SettingInfo(PreferencesTypes type) {
        this.type = type;
    }

    public PreferencesTypes getType() {
        return type;
    }
    public T getActualValue() {
        return actualValue;
    }

    public void setActualValue(T actualValue) {

        if (!type.getValueClass().isInstance(actualValue))
            throw new IllegalArgumentException(
                    "Invalid type for " + type
            );

        this.actualValue = actualValue;
    }
}
