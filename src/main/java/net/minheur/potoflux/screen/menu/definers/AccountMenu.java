package net.minheur.potoflux.screen.menu.definers;

import net.minheur.potoflux.translations.Translations;

import javax.swing.*;

import static net.minheur.potoflux.login.ConnectionHandler.*;

public class AccountMenu extends JMenu {

    private final JMenuItem openTab = new JMenuItem(Translations.get("potoflux:menu.account.openTab"));

    private final JMenuItem auth = new JMenuItem(getAuthButtonStatus());

    public AccountMenu() {
        super(Translations.get("common:account"));

        setupButtonActions();

        addAll();
    }

    private void setupButtonActions() {
        auth.addActionListener(e -> performAuthAction());
    }

    public void reload() {
        auth.setText(getAuthButtonStatus());
    }

    public void addAll() {
        add(openTab);
        add(auth);
    }

}
