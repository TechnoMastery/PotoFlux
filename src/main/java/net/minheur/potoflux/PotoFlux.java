package net.minheur.potoflux;

import net.minheur.potoflux.screen.PotoScreen;

import javax.swing.*;

public class PotoFlux {
    public static PotoScreen app;

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            app = new PotoScreen();
        });

    }
}
