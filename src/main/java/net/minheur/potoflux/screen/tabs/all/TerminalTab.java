package net.minheur.potoflux.screen.tabs.all;

import javafx.scene.layout.StackPane;
import net.minheur.potoflux.screen.tabs.BaseTab;
import net.minheur.potoflux.terminal.Terminal;
import net.minheur.potoflux.translations.Translations;

/**
 * The tab that contains the terminal of potoflux.
 */
public class TerminalTab extends BaseTab<StackPane> {
    /**
     * Actual var containing the terminal
     */
    private Terminal terminal;

    /**
     * Add content to the pane.<br>
     * Calls {@link #addTerminal()}
     */
    @Override
    protected void setPanel() {
        addTerminal();
    }

    /**
     * Instancies main panels
     */
    @Override
    protected void instantiate() {
        PANEL = new StackPane();
    }

    /**
     * Gets the name of the tab, displayed in the tab list
     *
     * @return the tab's name
     */
    @Override
    public String getName() {
        return Translations.get("potoflux:tabs.terminal.name");
    }

    /**
     * Disable the preset
     *
     * @return false
     */
    @Override
    protected boolean doPreset() {
        return false;
    }

    /**
     * Adds the terminal to the {@link #PANEL}
     */
    private void addTerminal() {
        terminal = new Terminal(PANEL);
    }

    /**
     * Getter for the {@link #terminal}
     *
     * @return the {@link #terminal}
     */
    public Terminal getTerminal() {
        return terminal;
    }
}
