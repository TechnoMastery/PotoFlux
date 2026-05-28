package net.minheur.potoflux.screen.menu.definers;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import net.minheur.potoflux.Functions;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.translations.Translations;
import net.minheur.potoflux.ui.UiUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MenuDefiners {

    public static @NotNull Menu getFileMenu() {

        // open mod dir
        MenuItem openModDir = new MenuItem(Translations.get("potoflux:menu.file.openModDir"));
        if (!Desktop.isDesktopSupported()) openModDir.setDisable(true);

        openModDir.setOnAction(e -> {

            File target = PotoFlux.getProgramDir().resolve("mods").toFile();

            if (!Functions.openDir(target)) UiUtils.showErrorPane(Translations.get("file:error.getDesktopFailed"));

        });

        // open log dir
        MenuItem openLogDir = new MenuItem(Translations.get("potoflux:menu.file.openLogDir"));
        if (!Desktop.isDesktopSupported()) openLogDir.setDisable(true);

        openLogDir.setOnAction(e -> {

            File target = PotoFlux.getProgramDir().resolve("logs").toFile();

            if (!Functions.openDir(target)) UiUtils.showErrorPane(Translations.get("file:error.getDesktopFailed"));

        });

        // main setup
        Menu fileMenu = new Menu(Translations.get("common:file"));

        fileMenu.getItems().add(openModDir);
        fileMenu.getItems().add(openLogDir);

        return fileMenu;
    }

}
