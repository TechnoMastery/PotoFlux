package net.minheur.potoflux.settings;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import net.minheur.potoflux.settings.types.ISettingType;
import net.minheur.potoflux.settings.types.PreferencesTypes;
import net.minheur.potoflux.translations.Translations;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.prefs.Preferences;

/**
 * This class is used to store and retrieve user preferences.
 */
public final class UserPrefsManager {
    /**
     * Actual prefs var for the user
     */
    private static final Preferences prefs = Preferences.userNodeForPackage(UserPrefsManager.class);

    /**
     * Lock the instanciation of the class
     */
    private UserPrefsManager() {}

    /**
     * Returns the actual value (or default if not set) for a given setting.
     * @param type the {@link PreferencesTypes} of the setting
     * @param defaultValue of the setting, if no value is set
     * @param id the ressource location of the setting. Is where the setting will be written and red from.
     * @return the value of the setting.
     * @throws ClassCastException if the default value is not the correct type for the type specified
     */
    public static Object getValueFor(@NotNull PreferencesTypes type, Object defaultValue, ResourceLocation id) {
        return switch (type) {
            case STRING -> prefs.get(id.toString(), ((String) defaultValue));
            case BOOLEAN -> prefs.getBoolean(id.toString(), ((Boolean) defaultValue));
            case INT -> prefs.getInt(id.toString(), ((Integer) defaultValue));
            case LONG -> prefs.getLong(id.toString(), ((Long) defaultValue));
            case FLOAT -> prefs.getFloat(id.toString(), ((Float) defaultValue));
            case DOUBLE -> prefs.getDouble(id.toString(), ((Double) defaultValue));
            case BYTE_ARRAY -> prefs.getByteArray(id.toString(), ((byte[]) defaultValue));
        };
    }
    /**
     * Returns the actual value for a given setting, from the {@link ISettingType}
     * @param type of the setting, used to get {@linkplain PreferencesTypes} and default
     * @param id the resource location used as key
     * @return the value of the setting.
     * @throws ClassCastException if the default value is not the correct type for the type specified
     */
    public static Object getValueFor(@NotNull ISettingType<?> type, ResourceLocation id) {
        return getValueFor(type.prefType(), type.getDefaultValue(), id);
    }
    /**
     * Returns the actual value (or default if not set) for a given setting.
     * @param setting used to get {@linkplain ISettingType}, id and default
     * @return the value of the setting
     * @throws ClassCastException if the default value is not the correct type for the type specified
     */
    public static Object getValueFor(@NotNull Setting setting) {
        return getValueFor(setting.type(), setting.id());
    }
    /**
     * Sets a setting value
     * @param id resource location used as key
     * @param type type of value, used to put the right type of setting
     * @param value of the setting, to be set
     */
    public static void setValueFor(ResourceLocation id, PreferencesTypes type, Object value) {

        if (value == null) throw new IllegalArgumentException("Can't have null value !");
        if (!type.getValueClass().isInstance(value))
            throw new IllegalArgumentException("Wrong type for " + type);

        switch (type) {
            case STRING -> prefs.put(id.toString(), (String) value);
            case BOOLEAN -> prefs.putBoolean(id.toString(), (Boolean) value);
            case INT -> prefs.putInt(id.toString(), (Integer) value);
            case LONG -> prefs.putLong(id.toString(), (Long) value);
            case FLOAT -> prefs.putFloat(id.toString(), (Float) value);
            case DOUBLE -> prefs.putDouble(id.toString(), (Double) value);
            case BYTE_ARRAY -> prefs.putByteArray(id.toString(), (byte[]) value);
        }

    }

    /**
     * Shows a popup to ask the user to restart the app
     */
    public static void showReload() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reload"); // TODO
        alert.setHeaderText(null);
        alert.setContentText(Translations.get("potoflux:prefs.reload"));
        alert.showAndWait();

        Platform.exit();
    }
}
