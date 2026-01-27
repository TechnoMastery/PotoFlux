package net.minheur.potoflux;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import net.minheur.potoflux.actionRuns.ActionRuns;
import net.minheur.potoflux.actionRuns.regs.ActionRun;
import net.minheur.potoflux.actionRuns.regs.CloseRunRegistry;
import net.minheur.potoflux.actionRuns.regs.StartLogicRunRegistry;
import net.minheur.potoflux.actionRuns.regs.StartUiRunRegistry;
import net.minheur.potoflux.loader.PotoFluxLoadingContext;
import net.minheur.potoflux.loader.mod.AddonLoader;
import net.minheur.potoflux.loader.mod.ModEventBus;
import net.minheur.potoflux.loader.mod.events.RegisterCommandsEvent;
import net.minheur.potoflux.loader.mod.events.RegisterLangEvent;
import net.minheur.potoflux.loader.mod.events.RegisterRunsEvent;
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
import net.minheur.potoflux.logger.PtfLogger;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;
import net.minheur.potoflux.utils.UserPrefsManager;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class PotoFlux {
    public static final String ID = "potoflux";

    public static PotoScreen app;
    public static void main(String[] args) {
        // env setup
        if (args.length < 1) PotoFluxLoadingContext.setDevEnv(false);
        else PotoFluxLoadingContext.setDevEnv(args[0].equals("devEnv"));

        if (PotoFluxLoadingContext.isDevEnv()) PtfLogger.info("App running in dev env !");

        // app version
        String version = getVersion();
        if (version != null) PtfLogger.info("Running potoflux v" + version);

        // load optional features
        PotoFluxLoadingContext.loadFeatures();

        // set theme
        String theme = UserPrefsManager.getTheme();
        if (theme.equals("dark")) FlatDarkLaf.setup(); // dark theme
        else if (theme.equals("light")) FlatLightLaf.setup(); // light theme
        else throw new IllegalStateException("Unknown theme: " + theme);
        PtfLogger.info("Theme set to " + theme);

        // load translations
        Translations.load(UserPrefsManager.getUserLang());

        // def modEventBus
        ModEventBus bus = PotoFluxLoadingContext.get().getModEventBus();

        // subscribe PotoFlux's data to modEventBus
        bus.addListener(PotoFlux::onRegisterLang);
        bus.addListener(Tabs::register);
        bus.addListener(Commands::register);
        bus.addListener(ActionRuns::register);

        // load all addons
        new AddonLoader().loadAddons();
        PotoFluxLoadingContext.loadMods();

        // post all registrations
        bus.post(new RegisterLangEvent()); // register lang BEFORE anything else
        bus.post(new RegisterTabsEvent());
        bus.post(new RegisterCommandsEvent());
        bus.post(new RegisterRunsEvent());

        // run all start logic runs
        for (ActionRun ar : StartLogicRunRegistry.getAll()) ar.run().run();

        // invoke app (start)
        SwingUtilities.invokeLater(() -> {
            app = new PotoScreen();

            // run all start ui runs
            for (ActionRun ar : StartUiRunRegistry.getAll()) ar.run().run();
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

        for (ActionRun ar : CloseRunRegistry.getAll()) ar.run().run();

        System.exit(exitCode); // close app
    }

    public static String getVersion() {
        try {
            Properties props = new Properties();
            props.load(PotoFlux.class.getResourceAsStream("/version.properties"));

            return props.getProperty("version");
        } catch (IOException e) {
            e.printStackTrace();
            PtfLogger.error("Could not get version !");
            return null;
        }
    }

    public static ResourceLocation fromModId(String loc) {
        return new ResourceLocation(ID, loc);
    }
}
