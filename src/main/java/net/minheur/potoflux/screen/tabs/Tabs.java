package net.minheur.potoflux.screen.tabs;

import net.minheur.potoflux.screen.tabs.all.*;
import net.minheur.potoflux.utils.Translations;

import java.util.ArrayList;
import java.util.List;

public class Tabs {
    public static final List<TabType> LIST = new ArrayList<>();

    public static final TabType HOME = new TabType("home", Translations.get("tabs.home.name"), HomeTab.class);
    public static final TabType TERMINAL = new TabType("terminal", Translations.get("tabs.term.name"), TerminalTab.class);
    public static final TabType TODO = new TabType("todo", Translations.get("tabs.todo.name"), TodoTab.class);
    public static final TabType CARD_LEARN = new TabType("card_learn", Translations.get("tabs.card.name"), CardLearningTab.class);
    public static final TabType SETTINGS = new TabType("settings", Translations.get("tabs.settings.title"), SettingsTab.class);

    public static void register() {
        TabRegistry.register(HOME);
        TabRegistry.register(TERMINAL);
        TabRegistry.register(TODO);
        TabRegistry.register(CARD_LEARN);
        TabRegistry.register(SETTINGS);
    }
}
