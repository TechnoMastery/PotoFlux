package net.minheur.potoflux.screen.menu.definers;

import net.minheur.potoflux.translations.Translations;

import javax.swing.*;

import static net.minheur.potoflux.login.ConnectionHandler.*;

public class AccountMenu extends JMenu {

    private final JMenuItem openTab = new JMenuItem(Translations.get("potoflux:menu.account.openTab"));
    private final JMenuItem auth = new JMenuItem(getAuthButtonStatus());

    private final JMenu perms = new JMenu(Translations.get("potoflux:menu.account.perms"));
    private final JMenuItem viewUsers = new JMenuItem(Translations.get("potoflux:perms.viewUsers"));
    private final JMenuItem createUsers = new JMenuItem(Translations.get("potoflux:menu.account.createUsers"));
    private final JMenuItem deleteUsers = new JMenuItem(Translations.get("potoflux:menu.account.deleteUsers"));
    private final JCheckBoxMenuItem accountCreationLock = new JCheckBoxMenuItem(Translations.get("potoflux:tabs.account.accountCreationState.title"));

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

        perms.add(viewUsers);
        perms.add(createUsers);
        perms.add(deleteUsers);
        perms.add(accountCreationLock);
        add(perms);
    }

}
