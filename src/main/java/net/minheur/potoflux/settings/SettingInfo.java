package net.minheur.potoflux.settings;

import net.minheur.potoflux.settings.types.PreferencesTypes;
import org.jetbrains.annotations.Nullable;

public class SettingInfo<T> {

    private final PreferencesTypes type;
    private @Nullable T actualValue;

    public boolean modified = false;

    public SettingInfo(PreferencesTypes type) {
        this.type = type;
    }

    public PreferencesTypes getType() {
        return type;
    }
    public @Nullable T getActualValue() {
        return actualValue;
    }

    @SuppressWarnings("unchecked")
    public void setActualValue(Object actualValue) {

        if (!type.getValueClass().isInstance(actualValue))
            throw new IllegalArgumentException(
                    "Invalid type for " + type
            );

        this.actualValue = (T) actualValue;
    }
}
