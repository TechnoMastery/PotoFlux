package net.minheur.potoflux;

import net.minheur.potoflux.screen.PotoScreen;
import net.minheur.potoflux.utils.Translations;
import net.minheur.potoflux.utils.UserPrefsManager;

import javax.swing.*;

public class PotoFlux {
    public static PotoScreen app;
    public static void main(String[] args) {
        Translations.load("en");
        Translations.load(UserPrefsManager.getUserLang());
        SwingUtilities.invokeLater(() -> {
            app = new PotoScreen();
        });

    }
}
