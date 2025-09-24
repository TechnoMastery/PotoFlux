package net.minheur.potoflux.utils;

import javax.swing.*;
import java.util.prefs.Preferences;

public class UserPrefsManager {
    private static final String KEY_LANG = "user_lang";
    private static final Preferences prefs = Preferences.userNodeForPackage(UserPrefsManager.class);

    public static String getUserLang() {
        String lang = prefs.get(KEY_LANG, null);
        if (lang == null) {
            lang = askUserLang();
            prefs.put(KEY_LANG, lang);
        }
        return lang;
    }

    private static String askUserLang() {
        String[] options = {"en", "fr"};
        String lang = JOptionPane.showInputDialog(
                null,
                Translations.get("prefs.langSelect"),
                Translations.get("prefs.language"),
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
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
