package net.minheur.potoflux.screen.tabs;

import net.minheur.potoflux.screen.tabs.all.*;
import net.minheur.potoflux.utils.Translations;

import java.util.ArrayList;
import java.util.List;

import static net.minheur.potoflux.PotoFlux.fromModId;

public class Tabs {
    private static final List<Tab> LIST = new ArrayList<>();

    public static final Tab HOME = LIST.add(new Tab(fromModId("home"), Translations.get("tabs.home.name"), HomeTab.class));
    public static final Tab TERMINAL = LIST.add(new Tab(fromModId("terminal"), Translations.get("tabs.term.name"), TerminalTab.class));
    public static final Tab TODO = LIST.add(new Tab(fromModId("todo"), Translations.get("tabs.todo.name"), TodoTab.class));
    public static final Tab CARD_LEARN = LIST.add(new Tab(fromModId("card_learn"), Translations.get("tabs.card.name"), CardLearningTab.class));
    public static final Tab SETTINGS = LIST.add(new Tab(fromModId("settings"), Translations.get("tabs.settings.title"), SettingsTab.class));

    public static void register() {

    }
}
