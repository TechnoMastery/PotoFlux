package net.minheur.potoflux.screen.tabs.all;

import net.minheur.potoflux.screen.tabs.BaseTab;
import net.minheur.potoflux.utils.Translations;
import net.minheur.potoflux.utils.UserPrefsManager;

import javax.swing.*;
import java.awt.*;

public class SettingsTab extends BaseTab {

    @Override
    protected void setPanel() {
        addLangButton();
        addAsciiButton();
    }

    private void addAsciiButton() {
        JButton asciiButton = new JButton(Translations.get("tabs.settings.asciiButton"));
        asciiButton.addActionListener(e -> {
            UserPrefsManager.resetTerminalAscii();
        });
        asciiButton.setFont(new Font("Consolas", Font.PLAIN, 15));
        asciiButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        PANEL.add(asciiButton);
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
