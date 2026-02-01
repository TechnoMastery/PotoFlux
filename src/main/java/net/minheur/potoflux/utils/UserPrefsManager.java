package net.minheur.potoflux.utils;

import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.terminal.CommandProcessor;
import net.minheur.potoflux.translations.Lang;
import net.minheur.potoflux.translations.Translations;

import javax.swing.*;
import java.util.Arrays;
import java.util.prefs.Preferences;

/**
 * This class is used to store and retrieve user preferences.
 */
public class UserPrefsManager {
    /**
     * Actual prefs var for the user
     */
    private static final Preferences prefs = Preferences.userNodeForPackage(UserPrefsManager.class);

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
        String lang = JOptionPane.showInputDialog(
                null,
                Translations.get("potoflux:prefs.lang.select"),
                Translations.get("potoflux:prefs.lang"),
                JOptionPane.QUESTION_MESSAGE,
                null,
                langOptions,
                langOptions[actualLangId]
        ).toString();
        if (lang == null) lang = "en";
        return lang;
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
        String ascii = JOptionPane.showInputDialog(
                null,
                Translations.get("potoflux:prefs.ascii.select"),
                Translations.get("potoflux:prefs.ascii"),
                JOptionPane.QUESTION_MESSAGE,
                null,
                asciiOptions,
                asciiOptions[actualAsciiId]
        ).toString();
        if (ascii == null) ascii = "en";
        return ascii;
    }

    /**
     * Asks the user for the theme
     * @return the chosen theme
     */
    private static String askUserTheme() {
        String actualTheme = getTheme();
        int actualThemeId = Arrays.asList(themeOptions).indexOf(actualTheme);

        String newTheme = JOptionPane.showInputDialog(
                null,
                Translations.get("potoflux:prefs.theme.select"),
                Translations.get("potoflux:prefs.theme"),
                JOptionPane.QUESTION_MESSAGE,
                null,
                themeOptions,
                themeOptions[actualThemeId]
        ).toString();

        if (newTheme == null) newTheme = actualTheme;
        return newTheme;
    }


    // resets
    /**
     * Runs the reset for the user lang
     */
    public static void resetUserLang() {
        prefs.put(KEY_LANG, askUserLang());
        showReload();
    }

    /**
     * Runs the reset for the terminal's ASCII
     */
    public static void resetTerminalAscii() {
        prefs.put(KEY_ASCII, askUserAscii());
        CommandProcessor.clearArea();
        showReload();
    }

    /**
     * Runs the reset for the theme
     */
    public static void resetTheme() {
        prefs.put(KEY_THEME, askUserTheme());
        showReload();
    }

    /**
     * Shows a popup to ask the user to restart the app
     */
    private static void showReload() {
        JOptionPane.showMessageDialog(null, Translations.get("potoflux:prefs.reload"));
        PotoFlux.runProgramClosing(0);
    }
}
