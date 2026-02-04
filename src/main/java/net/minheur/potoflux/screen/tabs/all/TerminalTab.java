package net.minheur.potoflux.screen.tabs.all;

import net.minheur.potoflux.screen.tabs.BaseTab;
import net.minheur.potoflux.terminal.Terminal;
import net.minheur.potoflux.translations.Translations;

/**
 * The tab that contains the terminal of potoflux.
 */
public class TerminalTab extends BaseTab {
    /**
     * Actual var containing the terminal
     */
    private Terminal terminal;

    /**
     * This is the actual method to set the panel.<br>
     * The overriding class will have to use this to add content to the {@link #PANEL}.
     */
    @Override
    protected void setPanel() {
        addTerminal();
    }

    /**
     * Disable the preset
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
     * @return the {@link #terminal}
     */
    public Terminal getTerminal() {
        return terminal;
    }
}
