package net.minheur.potoflux.utils;

import javax.swing.*;
import java.util.prefs.Preferences;

public class UserPrefsManager {
    private static final String KEY_LANG = "user_lang";

    public static String getUserLang() {
        Preferences prefs = Preferences.userNodeForPackage(UserPrefsManager.class);
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
                "Select language :",
                "Language",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        ).toString();
        if (lang == null) lang = "en";
        return lang;
    }

}
