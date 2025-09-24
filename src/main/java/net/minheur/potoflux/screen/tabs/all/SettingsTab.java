package net.minheur.potoflux.screen.tabs.all;

import net.minheur.potoflux.screen.tabs.BaseTab;
import net.minheur.potoflux.utils.Translations;
import net.minheur.potoflux.utils.UserPrefsManager;

import javax.swing.*;

public class SettingsTab extends BaseTab {
    @Override
    protected void setPanel() {
        addLangButton();
    }

    private void addLangButton() {
        JButton langButton = new JButton(Translations.get("tabs.settings.langButton"));
        langButton.addActionListener(e -> {
            UserPrefsManager.resetUserLang();
        });

        PANEL.add(langButton);
    }

    @Override
    protected String getTitle() {
        return Translations.get("tabs.settings.title");
    }
}
