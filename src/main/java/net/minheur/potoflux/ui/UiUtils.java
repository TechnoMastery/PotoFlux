package net.minheur.potoflux.ui;

import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.translations.Translations;

import javax.annotation.CheckForNull;
import javax.swing.*;

public class UiUtils {

    public static void showErrorPane(String message) {
        JOptionPane.showMessageDialog(
                getAppAnchor(),
                message,
                Translations.get("common:error"),
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void showMessagePane(String message) {
        JOptionPane.showMessageDialog(
                getAppAnchor(),
                message,
                Translations.get("common:info"),
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    @CheckForNull
    public static JFrame getAppAnchor() {
        return PotoFlux.app == null ?
                null : PotoFlux.app.getFrame();
    }

}
