package net.minheur.potoflux.settings;

import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.settings.types.ISettingType;
import net.minheur.potoflux.settings.types.PreferencesTypes;
import net.minheur.potoflux.terminal.CommandProcessor;
import net.minheur.potoflux.translations.Lang;
import net.minheur.potoflux.translations.Translations;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;

import java.util.Arrays;
import java.util.Optional;
import java.util.prefs.Preferences;

/**
 * This class is used to store and retrieve user preferences.
 */
public final class UserPrefsManager {
    /**
     * Actual prefs var for the user
     */
    private static final Preferences prefs = Preferences.userNodeForPackage(UserPrefsManager.class);

    private UserPrefsManager() {}

    /**
     * Return the actual value (or default if not set) if a given setting.
     * @param type the {@link ISettingType} of the setting, used to get the pref type and the default value
     * @param id the ressource location of the setting. Is where the setting will be written and red from.
     * @return the value of the setting.
     * @throws ClassCastException if the default value is not the correct type for the type specified
     */
    public static Object getValueFor(PreferencesTypes type, Object defaultValue, ResourceLocation id) {
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
    public static Object getValueFor(ISettingType<?> type, ResourceLocation id) {
        return getValueFor(type.prefType(), type.getDefaultValue(), id);
    }
    public static Object getValueFor(Setting setting) {
        return getValueFor(setting.type(), setting.id());
    }
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

    // lang
    /**
     * Key for storing the user language
     */
    private static final String KEY_LANG = "user_lang";
    /**
     * All values that the language var can take
     */
    private static final String[] langOptions = new String[Lang.values().length];
    static {
        for (int i = 0; i < Lang.values().length; i++) langOptions[i] = Lang.values()[i].code;
    }

    // ascii
    /**
     * Key for storing the user's terminal ASCII
     */
    private static final String KEY_ASCII = "terminal_ascii";
    /**
     * All values the user's terminal ASCII can take
     */
    private static final String[] asciiOptions = { "basic", "big", "chiseled" };

    // theme
    /**
     * Key for storing the user's theme
     */
    private static final String KEY_THEME = "app_theme";
    /**
     * All values the user's theme can take
     */
    private static final String[] themeOptions = { "dark", "light" };

    // getters
    /**
     * Getter for the terminal's ASCII
     * @return the terminal's ASCII
     */
    public static String getTerminalASCII() {
        return prefs.get(KEY_ASCII, null);
    }
    /**
     * Getter for the user lang (direct)
     * @return the user lang
     */
    public static String getStrictUserLang() {
        return prefs.get(KEY_LANG, null);
    }
    /**
     * Getter for the user lang (asking if null)
     * @return the user lang or asks the user for it
     */
    public static String getUserLang() {
        String lang = getStrictUserLang();
        if (lang == null) {
            while (lang == null)
                lang = askUserLang();
            prefs.put(KEY_LANG, lang);
        }
        return lang;
    }
    /**
     * Getter for the user's theme
     * @return the user's theme
     */
    public static String getTheme() {
        return prefs.get(KEY_THEME, "light");
    }

    // askers
    /**
     * Asks the user for the lang
     * @return the lang chosen
     */
    private static String askUserLang() {
        int actualLangId = 0;
        String strictLang = getStrictUserLang();
        if (strictLang != null) actualLangId = Arrays.asList(langOptions).indexOf(strictLang);
        if (actualLangId == -1) actualLangId = 0;

        ChoiceDialog<String> dialog = new ChoiceDialog<>(
                langOptions[actualLangId],
                langOptions
        );
        dialog.setTitle(Translations.get("potoflux:prefs.lang"));
        dialog.setHeaderText(Translations.get("potoflux:prefs.lang.select"));
        dialog.setContentText("New lang: "); // TODO

        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }

    /**
     * Asks the user for the terminal's ASCII
     * @return the chosen ASCII
     */
    private static String askUserAscii() {
        int actualAsciiId = 0;
        String strictAscii = getTerminalASCII();
        if (strictAscii != null) actualAsciiId = Arrays.asList(asciiOptions).indexOf(strictAscii);
        if (actualAsciiId == -1) actualAsciiId = 0;

        ChoiceDialog<String> dialog = new ChoiceDialog<>(
                asciiOptions[actualAsciiId],
                asciiOptions
        );
        dialog.setTitle(Translations.get("potoflux:prefs.ascii"));
        dialog.setHeaderText(Translations.get("potoflux:prefs.ascii.select"));
        dialog.setContentText("New ascii: "); // TODO

        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }

    /**
     * Asks the user for the theme
     * @return the chosen theme
     */
    private static String askUserTheme() {
        String actualTheme = getTheme();
        int actualThemeId = Arrays.asList(themeOptions).indexOf(actualTheme);

        ChoiceDialog<String> dialog = new ChoiceDialog<>(
                themeOptions[actualThemeId],
                themeOptions
        );
        dialog.setTitle(Translations.get("potoflux:prefs.theme"));
        dialog.setHeaderText(Translations.get("potoflux:prefs.theme.select"));
        dialog.setContentText("New theme: "); // TODO

        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }


    // resets
    /**
     * Runs the reset for the user lang
     */
    public static void resetUserLang() {
        String key = askUserLang();
        if (key == null) return;

        prefs.put(KEY_LANG, key);
        showReload();
    }

    /**
     * Runs the reset for the terminal's ASCII
     */
    public static void resetTerminalAscii() {
        String key = askUserAscii();
        if (key == null) return;

        prefs.put(KEY_ASCII, key);
        CommandProcessor.clearArea();
        showReload();
    }

    /**
     * Runs the reset for the theme
     */
    public static void resetTheme() {
        String key = askUserTheme();
        if (key == null) return;

        prefs.put(KEY_THEME, key);
        showReload();
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

        PotoFlux.runProgramClosing(0);
    }
}
