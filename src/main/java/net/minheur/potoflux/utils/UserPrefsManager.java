package net.minheur.potoflux.utils;

import javax.swing.*;
import java.util.Arrays;
import java.util.prefs.Preferences;

public class UserPrefsManager {
    private static final Preferences prefs = Preferences.userNodeForPackage(UserPrefsManager.class);
    // lang
    private static final String KEY_LANG = "user_lang";
    private static final String[] langOptions = { "en", "fr" };
    // ascii
    private static final String KEY_ASCII = "terminal_ascii";
    private static final String[] asciiOptions = { "basic", "big", "chiseled" };

    // getters
    public static String getTerminalASCII() {
        return prefs.get(KEY_ASCII, null);
    }
    public static String getStrictUserLang() {
        return prefs.get(KEY_LANG, null);
    }
    public static String getUserLang() {
        String lang = getStrictUserLang();
        if (lang == null) {
            lang = askUserLang();
            prefs.put(KEY_LANG, lang);
        }
        return lang;
    }

    private static String askUserLang() {
        int actualLangId = 0;
        String strictLang = getStrictUserLang();
        if (strictLang != null) actualLangId = Arrays.asList(langOptions).indexOf(strictLang);
        if (actualLangId == -1) actualLangId = 0;
        String lang = JOptionPane.showInputDialog(
                null,
                Translations.get("prefs.langSelect"),
                Translations.get("prefs.language"),
                JOptionPane.QUESTION_MESSAGE,
                null,
                langOptions,
                langOptions[actualLangId]
        ).toString();
        if (lang == null) lang = "en";
        return lang;
    }

    public static void resetUserLang() {
        prefs.put(KEY_LANG, askUserLang());
        JOptionPane.showMessageDialog(null, Translations.get("prefs.reload"));
        System.exit(0);
    }

}
