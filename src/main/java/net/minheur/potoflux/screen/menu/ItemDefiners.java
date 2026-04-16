package net.minheur.potoflux.screen.menu;

import net.minheur.potoflux.Functions;
import net.minheur.potoflux.PotoFlux;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ItemDefiners {

    static JMenu getFileMenu() {

        JMenuItem openModDir = new JMenuItem("Open mod dir");
        if (!Desktop.isDesktopSupported()) openModDir.setEnabled(false);

        openModDir.addActionListener(e -> {

            File target = PotoFlux.getProgramDir().resolve("mods").toFile();

            if (!Functions.openDir(target)) return; // TODO: error pane

        });

        JMenuItem openLogDir = new JMenuItem("Open log dir");
        if (!Desktop.isDesktopSupported()) openModDir.setEnabled(false);

        openLogDir.addActionListener(e -> {

            File target = PotoFlux.getProgramDir().resolve("logs").toFile();

            if (!Functions.openDir(target)) return; // TODO: error pane

        });

        JMenu fileMenu = new JMenu("File");

        fileMenu.add(openModDir);
        fileMenu.add(openLogDir);

        return fileMenu;
    }

}
