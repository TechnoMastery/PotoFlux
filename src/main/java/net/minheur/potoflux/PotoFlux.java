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
import net.minheur.potoflux.log.LogSaver;
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
import net.minheur.potoflux.utils.LogAmountManager;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;
import net.minheur.potoflux.utils.UserPrefsManager;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Main class for PotoFlux. This should be set as mainClass in Gradle.
 */
public class PotoFlux {
    /**
     * The ID for potoflux (namespace)
     */
    public static final String ID = "potoflux";
    /**
     * The actual app.<br>
     * This contains the JFrame and will be instantiated when the app will run.
     */
    public static PotoScreen app;

    /**
     * The main method, that runs PotoFlux.<br>
     * It will first check for args, then enable devEnv if args contains it.<br>
     * We then get version and log it, load all optionalFeatures, set the theme and set the loaded translations.<br>
     * We get the loading bus, register all into it then list and load mods.<br>
     * We post all events to the bus, and invoke the app.
     * @param args what you give to the app. Can contain 'devEnv' to enable PotoFlux's dev mod
     */
    public static void main(String[] args) {
        // env setup
        if (args.length < 1) PotoFluxLoadingContext.setDevEnv(false);
        else PotoFluxLoadingContext.setDevEnv(args[0].equals("devEnv"));

        // important inits
        LogSaver.init();
        LogAmountManager.init();

        if (PotoFluxLoadingContext.isDevEnv()) PtfLogger.info("App running in dev env !");

        // app version
        String version = getVersion();
        if (version != null) PtfLogger.info("Running potoflux v" + version);

        // load optional features
        PotoFluxLoadingContext.loadFeatures();

        // enable or not log saving
        runLogSavingEnablingLogic();

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

        PotoFluxLoadingContext.checkUpdates();
    }

    /**
     * Executes the logic to know if the log saving system will be enabled
     */
    private static void runLogSavingEnablingLogic() {
        String logSavingFeature = PotoFluxLoadingContext.getOptionalFeatures().getProperty("doLogSaving");

        if (logSavingFeature == null) enableLogSavingDefault();

        boolean isSavingEnabled = Boolean.parseBoolean(logSavingFeature);
        if (isSavingEnabled) LogSaver.enable();
    }
    /**
     * Executes the default logic to know if the log saving system will be enabled
     */
    private static void enableLogSavingDefault() {
        if (!PotoFluxLoadingContext.isDevEnv())
            LogSaver.enable();
    }

    /**
     * This register to the event all PotoFlux's translations
     * @param event the event for langs in the mod bus
     */
    private static void onRegisterLang(RegisterLangEvent event) {
        event.registerLang(new PotoFluxTranslations());
        event.registerLang(new CommonTranslations());
        event.registerLang(new FileTranslations());
    }

    /**
     * Getter for the main program AppData folder.
     * @return the program's appData folder
     */
    public static Path getProgramDir() {
        Path dir = Paths.get(System.getenv("APPDATA"), "TechnoMastery", "PotoFlux");
        try {
            Files.createDirectories(dir);
        } catch (IOException ignored) {}
        return dir;
    }

    /**
     * Getter for the mod common folder in the appData folder.<br>
     * Mods should use this to resolve their modIds: this will be their own appData folder
     * @return the shared appData folder for mods
     */
    public static Path getModDataDir() {
        Path dir = getProgramDir().resolve("mod-data");
        try {
            Files.createDirectories(dir);
        } catch (IOException ignored) {}
        return dir;
    }

    /**
     * This method should be used to close the app. This allows the app to run extra saving code before exiting.
     * @param exitCode the code given on closing.
     */
    public static void runProgramClosing(int exitCode) {
        // executes when program close

        for (ActionRun ar : CloseRunRegistry.getAll()) ar.run().run();

        // saves logs
        LogSaver.flushAndSave();

        System.exit(exitCode); // close app
    }

    /**
     * Getter for the app's version.<br>
     * If unable to get the version, return null.
     * @return the app version
     */
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

    /**
     * PotoFlux's reserved method to get a {@link ResourceLocation} directly with PotoFlux's modId.
     * @param loc the loc to add to the modId
     * @return a built {@link ResourceLocation} with potoflux's modId and the loc given
     */
    public static ResourceLocation fromModId(String loc) {
        return new ResourceLocation(ID, loc);
    }
}
