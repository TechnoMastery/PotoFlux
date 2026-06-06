package net.minheur.potoflux.settings;

import net.minheur.potoflux.settings.types.PreferencesTypes;
import org.jetbrains.annotations.Nullable;

/**
 * Container with infos about a setting
 * @param <T> type of the setting. One of {@link PreferencesTypes#valueClass}
 */
public class SettingInfo<T> {

    /**
     * Type of the setting, to check the value type
     */
    private final PreferencesTypes type;
    /**
     * Value of the setting, actually saved.<br>
     * Used to compare if the user changed the value.<br>
     * Is the type of {@linkplain #type}
     */
    private @Nullable T actualValue;

    /**
     * Creates the info with only a type.<br>
     * The value is set later via {@link #setActualValue(Object)}
     * @param type of the setting
     */
    public SettingInfo(PreferencesTypes type) {
        this.type = type;
    }

    /**
     * Getter for the {@link #type}
     * @return {@link #type}
     */
    public PreferencesTypes getType() {
        return type;
    }
    /**
     * Getter for the {@linkplain #actualValue}
     * @return {@link #actualValue}
     */
    public @Nullable T getActualValue() {
        return actualValue;
    }

    /**
     * Sets the value from an {@linkplain Object}. It needs to be the type of {@linkplain #type}
     * @param actualValue to set in {@link #actualValue}
     * @throws IllegalArgumentException if the given {@linkplain Object} isn't the type of {@linkplain #type}
     */
    @SuppressWarnings("unchecked")
    public void setActualValue(Object actualValue) {

        if (!type.getValueClass().isInstance(actualValue))
            throw new IllegalArgumentException(
                    "Invalid type for " + type
            );

        this.actualValue = (T) actualValue;
    }
}
