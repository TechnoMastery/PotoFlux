package net.minheur.potoflux.screen.tabs.all;

import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import net.minheur.potoflux.screen.tabs.BaseVTab;
import net.minheur.potoflux.translations.Translations;
import net.minheur.potoflux.utils.UserPrefsManager;

/**
 * The settings tab contains all user changeable vars (prefs)
 */
public class SettingsTab extends BaseVTab<StackPane> {
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

    @Override
    protected void instantiate() {
        PANEL = new StackPane();
    }

    /**
     * Adds the button to change the terminal's ASCII
     */
    private void addAsciiButton() {
        Button asciiButton = new Button(Translations.get("potoflux:prefs.ascii.button"));
        asciiButton.setOnAction(e -> {
            UserPrefsManager.resetTerminalAscii();
        });
        asciiButton.setFont(Font.font("Consolas", 15));
        // asciiButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        vContent.getChildren().add(asciiButton);
    }

    /**
     * Adds the button to change the app's lang
     */
    private void addLangButton() {
        Button langButton = new Button(Translations.get("potoflux:prefs.lang.button"));
        langButton.setOnAction(e -> {
            UserPrefsManager.resetUserLang();
        });
        langButton.setFont(Font.font("Consolas", 15));
        // langButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        vContent.getChildren().add(langButton);
    }

    /**
     * Adds the button to change the theme
     */
    private void addThemeButton() {
        Button themeButton = new Button(Translations.get("potoflux:prefs.theme.button"));
        themeButton.setOnAction(e -> {
            UserPrefsManager.resetTheme();
        });
        themeButton.setFont(Font.font("Consolas", 15));
        // themeButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        vContent.getChildren().add(themeButton);
    }

    /**
     * Used to set the title.
     * @return the title of the tab.
     */
    @Override
    protected String getTitle() {
        return Translations.get("potoflux:tabs.settings.name");
    }
}
