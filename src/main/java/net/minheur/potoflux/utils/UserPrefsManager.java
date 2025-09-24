package net.minheur.potoflux.utils;

import javax.swing.*;
import java.util.prefs.Preferences;

public class UserPrefsManager {
    private static final Preferences prefs = Preferences.userNodeForPackage(UserPrefsManager.class);
    // lang
    private static final String KEY_LANG = "user_lang";
    private static final String[] langOptions = { "en", "fr" };

    public static String getUserLang() {
        String lang = prefs.get(KEY_LANG, null);
        if (lang == null) {
            lang = askUserLang();
            prefs.put(KEY_LANG, lang);
        }
        return lang;
    }

    private static String askUserLang() {
        String lang = JOptionPane.showInputDialog(
                null,
                Translations.get("prefs.langSelect"),
                Translations.get("prefs.language"),
                JOptionPane.QUESTION_MESSAGE,
                null,
                langOptions,
                langOptions[0]
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
