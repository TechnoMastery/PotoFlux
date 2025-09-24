package net.minheur.potoflux.screen.tabs.all;

import net.minheur.potoflux.screen.tabs.BaseTab;
import net.minheur.potoflux.utils.Translations;
import net.minheur.potoflux.utils.UserPrefsManager;

import javax.swing.*;
import java.awt.*;

public class SettingsTab extends BaseTab {
    private final String USER_LANG = UserPrefsManager.getUserLang();

    @Override
    protected void setPanel() {
        addLangSelect();
        addLangButton();
    }

    private void addLangSelect() {

    }

    private void addLangButton() {
        JButton langButton = new JButton(Translations.get("tabs.settings.langButton"));
        langButton.addActionListener(e -> {
            UserPrefsManager.resetUserLang();
        });
        langButton.setFont(new Font("Consolas", Font.PLAIN, 15));
        langButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        PANEL.add(langButton);
    }

    @Override
    protected String getTitle() {
        return Translations.get("tabs.settings.title");
    }
}
