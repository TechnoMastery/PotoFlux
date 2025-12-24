package net.minheur.potoflux.screen.tabs;

import net.minheur.potoflux.loader.mod.events.RegisterTabsEvent;
import net.minheur.potoflux.registry.RegistryList;
import net.minheur.potoflux.screen.tabs.all.*;
import net.minheur.potoflux.translations.Translations;

import static net.minheur.potoflux.PotoFlux.fromModId;

public class Tabs {
    private final RegistryList<Tab> LIST = new RegistryList<>();
    private static boolean hasGenerated = false;

    public static Tabs INSTANCE;

    private Tabs() {
        if (hasGenerated) throw new IllegalStateException("Can't create the registry 2 times !");
        hasGenerated = true;
    }

    public final Tab HOME = LIST.add(new Tab(fromModId("home"), Translations.get("potoflux:tabs.home.name"), HomeTab.class));
    public final Tab TERMINAL = LIST.add(new Tab(fromModId("terminal"), Translations.get("potoflux:tabs.term.name"), TerminalTab.class));
    public final Tab SETTINGS = LIST.add(new Tab(fromModId("settings"), Translations.get("potoflux:tabs.settings.title"), SettingsTab.class));

    public static void register(RegisterTabsEvent event) {
        INSTANCE = new Tabs();

        INSTANCE.LIST.register(event.reg);
    }
}
