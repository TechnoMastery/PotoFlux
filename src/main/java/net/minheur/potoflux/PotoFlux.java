package net.minheur.potoflux;

import net.minheur.potoflux.loader.PotoFluxLoadingContext;
import net.minheur.potoflux.loader.mod.ModEventBus;
import net.minheur.potoflux.screen.PotoScreen;
import net.minheur.potoflux.screen.tabs.Tabs;
import net.minheur.potoflux.screen.tabs.all.TerminalTab;
import net.minheur.potoflux.terminal.CommandProcessor;
import net.minheur.potoflux.terminal.Commands;
import net.minheur.potoflux.utils.ResourceLocation;
import net.minheur.potoflux.utils.Translations;
import net.minheur.potoflux.utils.UserPrefsManager;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PotoFlux {
    public static final String ID = "potoflux";

    public static PotoScreen app;
    public static void main(String[] args) {
        Translations.load("en");
        Translations.load(UserPrefsManager.getUserLang());

        ModEventBus bus = PotoFluxLoadingContext.get().getModEventBus();
        bus.registerClass(Tabs.class);
        bus.registerClass(Commands.class);

        SwingUtilities.invokeLater(() -> {
            app = new PotoScreen();

            ((TerminalTab) app.getTabMap().get(Tabs.TERMINAL)).getTerminal().fillOutputTextArea();
        });
    }

    public static Path getProgramDir() {
        Path dir = Paths.get(System.getenv("APPDATA"), "TechnoMastery", "PotoFlux");
        try {
            Files.createDirectories(dir);
        } catch (IOException ignored) {}
        return dir;
    }

    public static void runProgramClosing(int exitCode) {
        // executes when program close

        CommandProcessor.runSaveTerminal();

        System.exit(exitCode); // close app
    }

    public static ResourceLocation fromModId(String loc) {
        return new ResourceLocation(ID, loc);
    }
}
