package net.minheur.potoflux.screen.tabs;

import net.minheur.potoflux.loader.mod.events.RegisterTabsEvent;
import net.minheur.potoflux.loader.mod.events.SubscribeEvent;
import net.minheur.potoflux.registry.RegistryList;
import net.minheur.potoflux.screen.tabs.all.*;
import net.minheur.potoflux.translations.Translations;

import static net.minheur.potoflux.PotoFlux.fromModId;

public class Tabs {
    private static final RegistryList<Tab> LIST = new RegistryList<>();

    public static final Tab HOME = LIST.add(new Tab(fromModId("home"), Translations.get("tabs.home.name"), HomeTab.class));
    public static final Tab TERMINAL = LIST.add(new Tab(fromModId("terminal"), Translations.get("tabs.term.name"), TerminalTab.class));
    public static final Tab TODO = LIST.add(new Tab(fromModId("todo"), Translations.get("tabs.todo.name"), TodoTab.class));
    public static final Tab CARD_LEARN = LIST.add(new Tab(fromModId("card_learn"), Translations.get("tabs.card.name"), CardLearningTab.class));
    public static final Tab SETTINGS = LIST.add(new Tab(fromModId("settings"), Translations.get("tabs.settings.title"), SettingsTab.class));

    @SubscribeEvent
    public static void register(RegisterTabsEvent event) {
        LIST.register(event.reg);
    }
}
