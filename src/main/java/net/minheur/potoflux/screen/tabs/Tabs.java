package net.minheur.potoflux.screen.tabs;

import net.minheur.potoflux.loader.mod.events.RegisterTabsEvent;
import net.minheur.potoflux.registry.RegistryList;
import net.minheur.potoflux.screen.tabs.all.*;
import net.minheur.potoflux.utils.SmartSupplier;
import org.jetbrains.annotations.NotNull;

import static net.minheur.potoflux.PotoFlux.fromModId;

/**
 * Registry of the potoflux tabs
 */
public class Tabs {
    /**
     * Registry list of the tabs
     */
    private static final RegistryList<Tab> LIST = new RegistryList<>();

    /**
     * The item of the home tab ({@link HomeTab}).
     */
    public static final SmartSupplier<Tab> HOME = LIST.add(() -> new Tab(fromModId("home"), HomeTab.class));
    /**
     * The item of the account tab ({@link AccountTab})
     */
    public static final SmartSupplier<Tab> ACCOUNT = LIST.add(() -> new Tab(fromModId("account"), AccountTab.class));
    /**
     * The item of the terminal tab ({@link TerminalTab}).
     */
    public static final SmartSupplier<Tab> TERMINAL = LIST.add(() -> new Tab(fromModId("terminal"), TerminalTab.class));
    public static final SmartSupplier<Tab> MODS = LIST.add(() -> new Tab(fromModId("mods"), ModsTab.class));
    /**
     * The item of the settings tab ({@link SettingsTab}).
     */
    public static final SmartSupplier<Tab> SETTINGS = LIST.add(() -> new Tab(fromModId("settings"), SettingsTab.class));

    public static final SmartSupplier<Tab> DEBUG = LIST.add(() -> new Tab(fromModId("debug"), DebugTab.class));

    /**
     * Create the instance of the class, then add the tabs to the events.<br>
     * This is called on the tab register event.
     *
     * @param event the event to register to
     */
    public static void register(@NotNull RegisterTabsEvent event) {
        LIST.register(event.reg);
    }
}
