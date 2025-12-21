package net.minheur.potoflux.screen.tabs;

import net.minheur.potoflux.loader.mod.events.RegisterTabsEvent;
import net.minheur.potoflux.registry.RegistryList;
import net.minheur.potoflux.screen.tabs.all.*;
import net.minheur.potoflux.translations.Translations;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;

import static net.minheur.potoflux.PotoFlux.fromModId;

public class Tabs {
    private final RegistryList<Tab> LIST = new RegistryList<>();
    private static boolean hasGenerated = false;

    public static Tabs INSTANCE;

    private Tabs() {
        if (hasGenerated) throw new IllegalStateException("Can't create the registry 2 times !");
        hasGenerated = true;
    }

    // public static final ResourceLocation HOME = fromModId("home");
    // public static final ResourceLocation TERMINAL = fromModId("terminal");
    // public static final ResourceLocation CARD_LEARN = fromModId("card_learn");
    // public static final ResourceLocation SETTINGS = fromModId("settings");

    public final Tab HOME = LIST.add(new Tab(fromModId("home"), Translations.get("potoflux:tabs.home.name"), HomeTab.class));
    public final Tab TERMINAL = LIST.add(new Tab(fromModId("terminal"), Translations.get("potoflux:tabs.term.name"), TerminalTab.class));
    public final Tab CARD_LEARN = LIST.add(new Tab(fromModId("card_learn"), Translations.get("potoflux:tabs.card.name"), CardLearningTab.class));
    public final Tab SETTINGS = LIST.add(new Tab(fromModId("settings"), Translations.get("potoflux:tabs.settings.title"), SettingsTab.class));

    private void createTabs() {
        // LIST.add(new Tab(HOME, Translations.get("potoflux:tabs.home.name"), HomeTab.class));
        // LIST.add(new Tab(TERMINAL, Translations.get("potoflux:tabs.term.name"), TerminalTab.class));
        // LIST.add(new Tab(CARD_LEARN, Translations.get("potoflux:tabs.card.name"), CardLearningTab.class));
        // LIST.add(new Tab(SETTINGS, Translations.get("potoflux:tabs.settings.title"), SettingsTab.class));
    }

    public static void register(RegisterTabsEvent event) {
        INSTANCE = new Tabs();
        INSTANCE.createTabs();

        INSTANCE.LIST.register(event.reg);
    }
}
