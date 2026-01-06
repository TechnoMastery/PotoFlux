package net.minheur.potoflux.screen.tabs.all;

import net.minheur.potoflux.Functions;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.screen.tabs.BaseTab;
import net.minheur.potoflux.translations.Translations;

import javax.swing.*;
import java.awt.*;

public class HomeTab extends BaseTab {
    @Override
    protected void setPanel() {
        addDesc();
        addVersion();
    }

    private void addDesc() {
        JLabel desc = new JLabel(Translations.get("potoflux:tabs.home.credit"));
        desc.setFont(new Font("Consolas", Font.PLAIN, 15));
        desc.setAlignmentX(Component.CENTER_ALIGNMENT);
        PANEL.add(desc);
    }

    private void addVersion() {
        String name = Functions.formatMessage(Translations.get("potoflux:tabs.home.version"),
                PotoFlux.getVersion());

        JLabel version = new JLabel(name);
        version.setFont(new Font("Consolas", Font.PLAIN, 15));
        version.setAlignmentX(Component.CENTER_ALIGNMENT);
        PANEL.add(version);
    }

    @Override
    protected String getTitle() {
        return Translations.get("potoflux:tabs.home.title");
    }
}
