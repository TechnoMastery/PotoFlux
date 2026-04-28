package net.minheur.potoflux.screen.menu.definers;

import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.login.ConnectionHandler;
import net.minheur.potoflux.login.perms.Perms;
import net.minheur.potoflux.screen.tabs.Tabs;
import net.minheur.potoflux.translations.Translations;

import javax.swing.*;

import java.util.Arrays;
import java.util.List;

import static net.minheur.potoflux.login.ConnectionHandler.*;

public class AccountMenu extends JMenu {

    private final JMenuItem openTab = new JMenuItem(Translations.get("potoflux:menu.account.openTab"));
    private final JMenuItem auth = new JMenuItem(getAuthButtonStatus());

    private final JMenu perms = new JMenu(Translations.get("potoflux:menu.account.perms"));
    private final JMenuItem viewUsers = new JMenuItem(Translations.get("potoflux:perms.viewUsers"));
    private final JMenuItem createUsers = new JMenuItem(Translations.get("potoflux:menu.account.createUsers"));
    private final JMenuItem deleteUsers = new JMenuItem(Translations.get("potoflux:menu.account.deleteUsers"));
    private final JCheckBoxMenuItem accountCreationLock = new JCheckBoxMenuItem(Translations.get("potoflux:menu.account.lockAccountCreation"));

    public AccountMenu() {
        super(Translations.get("common:account"));

        setupButtonActions();

        addAll();
    }

    private void setupButtonActions() {
        openTab.addActionListener(e -> PotoFlux.app.setOpenedTab(Tabs.INSTANCE.ACCOUNT));
        auth.addActionListener(e -> performAuthAction());

        viewUsers.addActionListener(e -> Perms.VIEW_USERS.getPermAction().run());
        createUsers.addActionListener(e -> Perms.CREATE_USERS.getPermAction().run());
        deleteUsers.addActionListener(e -> Perms.DELETE_USERS.getPermAction().run());
        accountCreationLock.addActionListener(e -> ConnectionHandler.sendAccountCreationLockRequest(accountCreationLock.isSelected()));
    }

    public void reload() {
        auth.setText(getAuthButtonStatus());

        if (isLogged) {
            List<Perms> allPerms = Arrays.stream(account.perms).toList();
            boolean addPerms = false;

            // setup perms
            if (!allPerms.contains(Perms.VIEW_USERS)) viewUsers.setVisible(false);
            else addPerms = true;
            if (!allPerms.contains(Perms.CREATE_USERS)) createUsers.setVisible(false);
            else addPerms = true;
            if (!allPerms.contains(Perms.DELETE_USERS)) deleteUsers.setVisible(false);
            else addPerms = true;
            if (!allPerms.contains(Perms.LOCK_ACCOUNT_CREATION)) accountCreationLock.setVisible(false);
            else addPerms = true;

            accountCreationLock.setSelected(isAccountCreationEnabled);
            if (!addPerms) perms.setVisible(false);

        } else perms.setVisible(false);

    }

    public void addAll() {
        add(openTab);
        add(auth);

        // perms
        perms.add(viewUsers);
        perms.add(createUsers);
        perms.add(deleteUsers);
        perms.add(accountCreationLock);
        add(perms);
    }

}
