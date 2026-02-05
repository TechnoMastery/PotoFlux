package net.minheur.potoflux.screen.tabs;

import javax.swing.*;
import java.awt.*;

/**
 * The base tab, overridden to create your own tabs.
 */
public abstract class BaseTab {
    /**
     * The actual {@link JPanel}, that will be added to the tabbed pane.
     */
    protected final JPanel PANEL = new JPanel();

    /**
     * Constructor for the tab.<br>
     * It will do the preset if enabled, and execute (or invokeLater) the {@link #setPanel()} method, to add data to the {@link #PANEL}.
     */
    public BaseTab() {
        if (doPreset()) preset();
        if (invokeLater()) SwingUtilities.invokeLater(this::setPanel);
        else setPanel();
    }

    /**
     * The preset, that is by default enabled.<br>
     * It sets the layout and add the title.
     */
    protected void preset() {
        PANEL.setLayout(new BoxLayout(PANEL, BoxLayout.Y_AXIS));
        PANEL.add(Box.createVerticalStrut(30));
        createTitle();
        PANEL.add(Box.createVerticalStrut(20));
    }

    /**
     * This is the actual method to set the panel.<br>
     * The overriding class will have to use this to add content to the {@link #PANEL}.
     */
    protected abstract void setPanel();
    /**
     * Used to set the title.
     * @return the title of the tab.
     */
    protected String getTitle() {
        return "[NO TITLE SET]";
    }

    /**
     * Getter for the {@link #PANEL}.
     * @return the {@link #PANEL}
     */
    public JPanel getPanel() {
        return PANEL;
    }

    /**
     * Handles if the preset should be executed.<br>
     * True by default.
     * @return if the preset should be executed
     */
    protected boolean doPreset() {
        return true;
    }

    /**
     * Handles if the creation should be added to the swing EDT, otherwise it will be executed on the tab creation.
     * @return if the app should be {@link SwingUtilities#invokeLater(Runnable)}.
     */
    @Deprecated(since = "6.4")
    protected boolean invokeLater() {
        return false;
    }

    /**
     * Creates the title, from the text given via {@link #getTitle()}.<br>
     * It is used in the {@link #preset()}
     */
    protected void createTitle() {
        JLabel title = new JLabel(getTitle());
        title.setFont(new Font("Consolas", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        PANEL.add(title);
    }
}
