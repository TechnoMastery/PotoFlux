package net.minheur.potoflux.screen.tabs.all;

import net.minheur.potoflux.screen.tabs.BaseTab;
import net.minheur.potoflux.translations.Translations;
import net.minheur.potoflux.utils.UserPrefsManager;

import javax.swing.*;
import java.awt.*;

/**
 * The settings tab contains all user changeable vars (prefs)
 */
public class SettingsTab extends BaseTab {
    /**
     * This is the actual method to set the panel.<br>
     * The overriding class will have to use this to add content to the {@link #PANEL}.
     */
    @Override
    protected void setPanel() {
        addLangButton();
        addAsciiButton();
        addThemeButton();
    }

    /**
     * Adds the button to change the terminal's ASCII
     */
    private void addAsciiButton() {
        JButton asciiButton = new JButton(Translations.get("potoflux:prefs.ascii.button"));
        asciiButton.addActionListener(e -> {
            UserPrefsManager.resetTerminalAscii();
        });
        asciiButton.setFont(new Font("Consolas", Font.PLAIN, 15));
        asciiButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        PANEL.add(asciiButton);
    }

    /**
     * Adds the button to change the app's lang
     */
    private void addLangButton() {
        JButton langButton = new JButton(Translations.get("potoflux:prefs.lang.button"));
        langButton.addActionListener(e -> {
            UserPrefsManager.resetUserLang();
        });
        langButton.setFont(new Font("Consolas", Font.PLAIN, 15));
        langButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        PANEL.add(langButton);
    }

    /**
     * Adds the button to change the theme
     */
    private void addThemeButton() {
        JButton themeButton = new JButton(Translations.get("potoflux:prefs.theme.button"));
        themeButton.addActionListener(e -> {
            UserPrefsManager.resetTheme();
        });
        themeButton.setFont(new Font("Consolas", Font.PLAIN, 15));
        themeButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        PANEL.add(themeButton);
    }

    /**
     * Used to set the title.
     * @return the title of the tab.
     */
    @Override
    protected String getTitle() {
        return Translations.get("potoflux:tabs.settings.title");
    }
}
