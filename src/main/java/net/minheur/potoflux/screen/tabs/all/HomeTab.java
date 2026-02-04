package net.minheur.potoflux.screen.tabs.all;

import net.minheur.potoflux.Functions;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.screen.tabs.BaseTab;
import net.minheur.potoflux.translations.Translations;

import javax.swing.*;
import java.awt.*;

/**
 * Tab class for the home tab
 */
public class HomeTab extends BaseTab {
    /**
     * This is the actual method to set the panel.<br>
     * The overriding class will have to use this to add content to the {@link #PANEL}.
     */
    @Override
    protected void setPanel() {
        addDesc();
        addVersion();
    }

    /**
     * Adds the description of the app
     */
    private void addDesc() {
        JLabel desc = new JLabel(Translations.get("potoflux:tabs.home.credit"));
        desc.setFont(new Font("Consolas", Font.PLAIN, 15));
        desc.setAlignmentX(Component.CENTER_ALIGNMENT);
        PANEL.add(desc);
    }

    /**
     * Adds the version of the app
     */
    private void addVersion() {
        String name = Functions.formatMessage(Translations.get("potoflux:tabs.home.version"),
                PotoFlux.getVersion());

        JLabel version = new JLabel(name);
        version.setFont(new Font("Consolas", Font.PLAIN, 15));
        version.setAlignmentX(Component.CENTER_ALIGNMENT);
        PANEL.add(version);
    }

    /**
     * Used to set the title.
     * @return the title of the tab.
     */
    @Override
    protected String getTitle() {
        return Translations.get("potoflux:tabs.home.title");
    }
}
