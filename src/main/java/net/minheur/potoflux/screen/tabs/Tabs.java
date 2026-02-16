package net.minheur.potoflux.screen.tabs;

import net.minheur.potoflux.loader.PotoFluxLoadingContext;
import net.minheur.potoflux.loader.mod.events.RegisterTabsEvent;
import net.minheur.potoflux.registry.RegistryList;
import net.minheur.potoflux.screen.tabs.all.*;
import net.minheur.potoflux.translations.Translations;

import static net.minheur.potoflux.PotoFlux.fromModId;

/**
 * Registry of the potoflux tabs
 */
public class Tabs {
    /**
     * Registry list of the tabs
     */
    private final RegistryList<Tab> LIST = new RegistryList<>();
    /**
     * Checks if the list has already been added to the reg.
     */
    private static boolean hasGenerated = false;

    /**
     * The instance of this class, used to keep a link to the tab def
     */
    public static Tabs INSTANCE;

    /**
     * Private builder, used to check that the tabs are not added twice to the reg.
     */
    private Tabs() {
        if (hasGenerated) throw new IllegalStateException("Can't create the registry 2 times !");
        hasGenerated = true;
    }

    /**
     * The item of the home tab ({@link HomeTab}).
     */
    public final Tab HOME = LIST.add(new Tab(fromModId("home"), Translations.get("potoflux:tabs.home.name"), HomeTab.class));
    /**
     * The item of the terminal tab ({@link TerminalTab}).
     */
    public final Tab TERMINAL = LIST.add(new Tab(fromModId("terminal"), Translations.get("potoflux:tabs.terminal.name"), TerminalTab.class));
    /**
     * The item of the settings tab ({@link SettingsTab}).
     */
    public final Tab SETTINGS = LIST.add(new Tab(fromModId("settings"), Translations.get("potoflux:tabs.settings.name"), SettingsTab.class));
    /**
     * The item of the catalog tab ({@link CatalogTab}).
     */
    public final Tab CATALOG =
            Boolean.parseBoolean(PotoFluxLoadingContext.getOptionalFeatures().getProperty("catalogTab", "false")) ?
            LIST.add(new Tab(fromModId("catalog"), Translations.get("potoflux:tabs.catalog.name"), CatalogTab.class)) : null;

    /**
     * Create the instance of the class, then add the tabs to the events.<br>
     * This is called on the tab register event.
     * @param event the event to register to
     */
    public static void register(RegisterTabsEvent event) {
        INSTANCE = new Tabs();

        INSTANCE.LIST.register(event.reg);
    }
}
