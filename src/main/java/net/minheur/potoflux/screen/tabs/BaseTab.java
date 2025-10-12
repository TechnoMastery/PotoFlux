package net.minheur.potoflux.screen.tabs;

import javax.swing.*;
import java.awt.*;

public abstract class BaseTab {
    protected final JPanel PANEL = new JPanel();

    public BaseTab() {
        if (doPreset()) preset();
        SwingUtilities.invokeLater(this::setPanel);
    }

    protected void preset() {
        PANEL.setLayout(new BoxLayout(PANEL, BoxLayout.Y_AXIS));
        PANEL.add(Box.createVerticalStrut(30));
        createTitle();
        PANEL.add(Box.createVerticalStrut(20));
    }

    protected abstract void setPanel();
    protected abstract String getTitle();

    public JPanel getPanel() {
        return PANEL;
    }

    protected boolean doPreset() {
        return true;
    }

    protected void createTitle() {
        JLabel title = new JLabel(getTitle());
        title.setFont(new Font("Consolas", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        PANEL.add(title);
    }
}
