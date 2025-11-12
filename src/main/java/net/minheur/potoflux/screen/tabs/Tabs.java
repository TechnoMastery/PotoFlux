package net.minheur.potoflux.screen.tabs;

import net.minheur.potoflux.screen.tabs.all.*;
import net.minheur.potoflux.utils.Translations;

import javax.swing.*;

public enum Tabs {
    // note : l'ordre de l'enum ici est celui des tabs
    HOME(HomeTab.class, Translations.get("tabs.home.name")),
    TERMINAL(TerminalTab.class, Translations.get("tabs.term.name")),
    TODO(TodoTab.class, Translations.get("tabs.todo.name")),
    CARD_LEARNING(CardLearningTab.class, Translations.get("tabs.card.name")),
    MC_MOD_GEN(MinecraftModGenTab.class, "Minecraft mod Gen"),
    SETTINGS(SettingsTab.class, Translations.get("tabs.settings.title"));

    private final Class<? extends BaseTab> associatedClass;
    private final String name;

    Tabs(Class<? extends BaseTab> associatedClass, String name) {
        this.associatedClass = associatedClass;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Class<? extends BaseTab> getAssociatedClass() {
        return associatedClass;
    }

    public BaseTab createInstance() {
        try {
            return associatedClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, Translations.get("tabs.createError") + name + " : " + e.getMessage());
            return null;
        }
    }
}
