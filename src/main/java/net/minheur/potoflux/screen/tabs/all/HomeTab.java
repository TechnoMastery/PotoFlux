package net.minheur.potoflux.screen.tabs.all;

import net.minheur.potoflux.screen.tabs.BaseTab;
import net.minheur.potoflux.translations.Translations;

import javax.swing.*;
import java.awt.*;

public class HomeTab extends BaseTab {
    @Override
    protected void setPanel() {
        addDesc();
    }

    private void addDesc() {
        JLabel desc = new JLabel(Translations.get("tabs.home.credit"));
        desc.setFont(new Font("Consolas", Font.PLAIN, 15));
        desc.setAlignmentX(Component.CENTER_ALIGNMENT);
        PANEL.add(desc);
    }

    @Override
    protected String getTitle() {
        return Translations.get("tabs.home.title");
    }
}
