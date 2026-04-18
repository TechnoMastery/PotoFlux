package net.minheur.potoflux.screen.menu.definers;

import net.minheur.potoflux.Functions;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.screen.tabs.Tabs;
import net.minheur.potoflux.translations.Translations;
import net.minheur.potoflux.ui.UiUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import static net.minheur.potoflux.login.ConnectionHandler.*;

public class MenuDefiners {

    public static JMenu getFileMenu() {

        // open mod dir
        JMenuItem openModDir = new JMenuItem(Translations.get("potoflux:menu.file.openModDir"));
        if (!Desktop.isDesktopSupported()) openModDir.setEnabled(false);

        openModDir.addActionListener(e -> {

            File target = PotoFlux.getProgramDir().resolve("mods").toFile();

            if (!Functions.openDir(target)) UiUtils.showErrorPane(Translations.get("file:error.getDesktopFailed"));

        });

        // open log dir
        JMenuItem openLogDir = new JMenuItem(Translations.get("potoflux:menu.file.openLogDir"));
        if (!Desktop.isDesktopSupported()) openModDir.setEnabled(false);

        openLogDir.addActionListener(e -> {

            File target = PotoFlux.getProgramDir().resolve("logs").toFile();

            if (!Functions.openDir(target)) UiUtils.showErrorPane(Translations.get("file:error.getDesktopFailed"));

        });

        // main setup
        JMenu fileMenu = new JMenu(Translations.get("common:file"));

        fileMenu.add(openModDir);
        fileMenu.add(openLogDir);

        return fileMenu;
    }

    public static JMenu getAccountMenu() {

        // open tab
        JMenuItem openAccountTab = new JMenuItem(Translations.get("potoflux:menu.account.openTab"));
        openAccountTab.addActionListener(e -> PotoFlux.app.setOpenedTab(Tabs.INSTANCE.ACCOUNT));

        // log in/out
        JMenuItem auth = new JMenuItem(isLogged ?
                Translations.get("potoflux:menu.account.logout") :
                Translations.get("potoflux:menu.account.login"));

        // main setup
        JMenu accountMenu = new JMenu(Translations.get("common:account"));

        accountMenu.add(openAccountTab);

        return accountMenu;

    }

}
