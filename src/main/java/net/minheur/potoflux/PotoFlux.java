package net.minheur.potoflux;

import net.minheur.potoflux.terminal.CommandProcessor;
import net.minheur.potoflux.terminal.Terminal;

import javax.swing.*;

public class PotoFlux {
    public static Terminal app;

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            app = new Terminal();
        });

        Functions.logInfo("info test");
        Functions.logError("info test");

    }
}
