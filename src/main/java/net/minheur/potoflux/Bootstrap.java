package net.minheur.potoflux;

import net.minheur.potoflux.actionRuns.ActionRuns;
import net.minheur.potoflux.actionRuns.regs.ActionRun;
import net.minheur.potoflux.actionRuns.regs.StartLogicRunRegistry;
import net.minheur.potoflux.loader.PotoFluxLoadingContext;
import net.minheur.potoflux.loader.mod.AddonLoader;
import net.minheur.potoflux.loader.mod.ModEventBus;
import net.minheur.potoflux.loader.mod.events.*;
import net.minheur.potoflux.logger.LogSaver;
import net.minheur.potoflux.logger.PtfLogger;
import net.minheur.potoflux.screen.menu.MenuContent;
import net.minheur.potoflux.screen.tabs.Tabs;
import net.minheur.potoflux.settings.OptionalFeaturesManager;
import net.minheur.potoflux.settings.Settings;
import net.minheur.potoflux.settings.UserPrefsManager;
import net.minheur.potoflux.settings.types.PreferencesTypes;
import net.minheur.potoflux.terminal.commands.Commands;
import net.minheur.potoflux.translations.Lang;
import net.minheur.potoflux.translations.Translations;
import net.minheur.potoflux.translations.register.CommonTranslations;
import net.minheur.potoflux.translations.register.FileTranslations;
import net.minheur.potoflux.translations.register.PotoFluxTranslations;
import net.minheur.potoflux.utils.LogAmountManager;
import net.minheur.potoflux.utils.close.EventPostException;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static net.minheur.potoflux.PotoFlux.fromModId;

public class Bootstrap {

    private static final AtomicBoolean built = new AtomicBoolean(false);

    /**
     * It will first check for args, then enable devEnv if args contains it.<br>
     * We then get version and log it, load all optionalFeatures, set the theme and set the loaded translations.<br>
     * We get the loading bus, register all into it then list and load mods.<br>
     * We post all events to the bus, and invoke the app.
     * @param updateText consumer that allows to change the text of the loading screen
     * @param args args given on app startup
     */
    public static void bootstrap(Consumer<String> updateText, String[] args) {
        if (!built.compareAndSet(false, true)) return;

        // env setup
        updateText.accept("Loading environment...");
        if (args.length < 1) PotoFluxLoadingContext.setDevEnv(false);
        else PotoFluxLoadingContext.setDevEnv(args[0].equals("devEnv"));

        // important inits
        updateText.accept(("Init..."));
        LogSaver.init();
        LogAmountManager.init();

        if (PotoFluxLoadingContext.isDevEnv()) PtfLogger.info("App running in dev env !");

        // app version
        updateText.accept("Getting version...");
        String version = PotoFlux.getVersion();
        if (version != null) PtfLogger.info("Running potoflux v" + version);

        // load optional features
        updateText.accept("Loading features...");
        OptionalFeaturesManager.load();

        // enable or not log saving
        updateText.accept("Loading log logic...");
        runLogSavingEnablingLogic();

        // set theme todo
        // updateText.accept("Getting the theme...");
        // String theme = UserPrefsManager.getTheme();
        // if (theme.equals("dark")) FlatDarkLaf.setup(); // dark theme
        // else if (theme.equals("light")) FlatLightLaf.setup(); // light theme
        // else throw new IllegalStateException("Unknown theme: " + theme);
        // PtfLogger.info("Theme set to " + theme);

        // load translations
        updateText.accept("Loading translations...");
        String lang = (String) UserPrefsManager.getValueFor(PreferencesTypes.STRING, Lang.EN.code, fromModId("lang"));
        Translations.load(lang);

        // def modEventBus
        updateText.accept("Loading event bus...");
        ModEventBus bus = PotoFluxLoadingContext.get().getModEventBus();

        // subscribe PotoFlux's data to modEventBus
        bus.addListener(Bootstrap::onRegisterLang);
        bus.addListener(Tabs::register);
        bus.addListener(Commands::register);
        bus.addListener(ActionRuns::register);
        bus.addListener(MenuContent::register);
        bus.addListener(Settings::register);

        // load all addons
        updateText.accept("Loading addons...");
        new AddonLoader().loadAddons();
        PotoFluxLoadingContext.loadMods();

        // post all registrations
        updateText.accept("Registering data...");

        try {
            bus.post(new RegisterLangEvent()); // register lang BEFORE anything else

            bus.post(new RegisterTabsEvent());
            bus.post(new RegisterCommandsEvent());
            bus.post(new RegisterRunsEvent());
            bus.post(new RegisterMenuEvent());
            bus.post(new RegisterSettingEvent());
        } catch (Throwable e) {
            throw new EventPostException(e);
        }

        // run all start logic runs
        updateText.accept("Running start logic...");
        for (ActionRun ar : StartLogicRunRegistry.getAll()) ar.run().run();

    }

    /**
     * Executes the logic to know if the log saving system will be enabled
     */
    private static void runLogSavingEnablingLogic() {
        Boolean logSavingFeature = OptionalFeaturesManager.getBoolean("doLogSaving");

        if (logSavingFeature == null) enableLogSavingDefault();
        else if (logSavingFeature) LogSaver.enable();
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
    private static void onRegisterLang(@NotNull RegisterLangEvent event) {
        event.registerLang(new PotoFluxTranslations());
        event.registerLang(new CommonTranslations());
        event.registerLang(new FileTranslations());
    }

}
