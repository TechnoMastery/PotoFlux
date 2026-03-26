package net.minheur.potoflux.ui;

import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.translations.Translations;

import javax.swing.*;

public class UiUtils {

    public static void showErrorPane(String message) {
        JOptionPane.showMessageDialog(
                PotoFlux.app.getFrame(),
                message,
                Translations.get("common:error"),
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void showMessagePane(String message) {
        JOptionPane.showMessageDialog(
                PotoFlux.app.getFrame(),
                message,
                Translations.get("common:info"),
                JOptionPane.INFORMATION_MESSAGE
        );
    }

}
