package net.minheur.potoflux;

import net.minheur.potoflux.loader.PotoFluxLoadingContext;
import net.minheur.potoflux.loader.mod.AddonLoader;
import net.minheur.potoflux.loader.mod.ModEventBus;
import net.minheur.potoflux.loader.mod.events.RegisterCommandsEvent;
import net.minheur.potoflux.loader.mod.events.RegisterLangEvent;
import net.minheur.potoflux.loader.mod.events.RegisterTabsEvent;
import net.minheur.potoflux.screen.PotoScreen;
import net.minheur.potoflux.screen.tabs.Tabs;
import net.minheur.potoflux.screen.tabs.all.TerminalTab;
import net.minheur.potoflux.terminal.CommandProcessor;
import net.minheur.potoflux.terminal.commands.Commands;
import net.minheur.potoflux.translations.Translations;
import net.minheur.potoflux.translations.register.CommonTranslations;
import net.minheur.potoflux.translations.register.FileTranslations;
import net.minheur.potoflux.translations.register.PotoFluxTranslations;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;
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
        // load translations
        Translations.load(UserPrefsManager.getUserLang());

        // def modEventBus
        ModEventBus bus = PotoFluxLoadingContext.get().getModEventBus();

        // load all addons
        new AddonLoader().loadAddons();

        // subscribe PotoFlux's data to modEventBus
        bus.addListener(PotoFlux::onRegisterLang);
        bus.addListener(Tabs::register);
        bus.addListener(Commands::register);

        // post all registrations
        bus.post(new RegisterLangEvent()); // register lang BEFORE anything else
        bus.post(new RegisterTabsEvent());
        bus.post(new RegisterCommandsEvent());

        // invoke app (start)
        SwingUtilities.invokeLater(() -> {
            app = new PotoScreen();

            ((TerminalTab) app.getFromResourceLoc(Tabs.TERMINAL)).getTerminal().fillOutputTextArea();
        });
    }

    private static void onRegisterLang(RegisterLangEvent event) {
        event.registerLang(new PotoFluxTranslations());
        event.registerLang(new CommonTranslations());
        event.registerLang(new FileTranslations());
    }

    public static Path getProgramDir() {
        Path dir = Paths.get(System.getenv("APPDATA"), "TechnoMastery", "PotoFlux");
        try {
            Files.createDirectories(dir);
        } catch (IOException ignored) {}
        return dir;
    }

    public static Path getModDataDir() {
        Path dir = getProgramDir().resolve("mod-data");
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
