package net.minheur.potoflux.screen.tabs;

import net.minheur.potoflux.loader.mod.events.RegisterTabsEvent;
import net.minheur.potoflux.registry.RegistryList;
import net.minheur.potoflux.screen.tabs.all.*;
import net.minheur.potoflux.translations.Translations;

import static net.minheur.potoflux.PotoFlux.fromModId;

public class Tabs {
    private static final RegistryList<Tab> LIST = new RegistryList<>();

    public static final Tab HOME = LIST.add(new Tab(fromModId("home"), Translations.get("potoflux:tabs.home.name"), HomeTab.class));
    public static final Tab TERMINAL = LIST.add(new Tab(fromModId("terminal"), Translations.get("potoflux:tabs.term.name"), TerminalTab.class));
    public static final Tab TODO = new Tab(fromModId("todo"), Translations.get("potoflux:tabs.todo.name"), TodoTab.class);
    public static final Tab CARD_LEARN = LIST.add(new Tab(fromModId("card_learn"), Translations.get("potoflux:tabs.card.name"), CardLearningTab.class));
    public static final Tab SETTINGS = LIST.add(new Tab(fromModId("settings"), Translations.get("potoflux:tabs.settings.title"), SettingsTab.class));

    public static void register(RegisterTabsEvent event) {
        LIST.register(event.reg);
    }
}
