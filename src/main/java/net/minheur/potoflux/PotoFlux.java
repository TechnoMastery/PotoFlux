package net.minheur.potoflux;

import net.minheur.potoflux.screen.PotoScreen;
import net.minheur.potoflux.translations.Translations;

import javax.swing.*;

public class PotoFlux {
    public static PotoScreen app;

    public static void main(String[] args) {
        Translations.load("en");
        Translations.load("fr");
        SwingUtilities.invokeLater(() -> {
            app = new PotoScreen();
        });

    }
}
