package net.minheur.potoflux.screen.tabs.all;

import net.minheur.potoflux.screen.tabs.BaseTab;
import net.minheur.potoflux.terminal.Terminal;
import net.minheur.potoflux.utils.Translations;

public class TerminalTab extends BaseTab {
    private Terminal terminal;

    @Override
    protected void setPanel() {
        addTerminal();
    }

    @Override
    protected boolean doPreset() {
        return false;
    }

    private void addTerminal() {
        terminal = new Terminal(PANEL);
    }

    @Override
    protected String getTitle() {
        return Translations.get("tabs.term.title");
    }

    public Terminal getTerminal() {
        return terminal;
    }
}
