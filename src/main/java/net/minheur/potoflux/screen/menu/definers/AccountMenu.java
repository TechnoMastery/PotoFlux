package net.minheur.potoflux.screen.menu.definers;

import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.login.ConnectionHandler;
import net.minheur.potoflux.login.perms.Perms;
import net.minheur.potoflux.screen.tabs.Tabs;
import net.minheur.potoflux.translations.Translations;

import javax.swing.*;

import java.util.Arrays;
import java.util.List;

import static net.minheur.potoflux.login.ConnectionHandler.*;

public class AccountMenu extends Menu {

    private final MenuItem openTab = new MenuItem(Translations.get("potoflux:menu.account.openTab"));
    private final MenuItem auth = new MenuItem(getAuthButtonStatus());

    private final Menu perms = new Menu(Translations.get("potoflux:menu.account.perms"));
    private final MenuItem viewUsers = new MenuItem(Translations.get("potoflux:perms.viewUsers"));
    private final MenuItem createUsers = new MenuItem(Translations.get("potoflux:menu.account.createUsers"));
    private final MenuItem deleteUsers = new MenuItem(Translations.get("potoflux:menu.account.deleteUsers"));
    private final CheckMenuItem accountCreationLock = new CheckMenuItem(Translations.get("potoflux:menu.account.lockAccountCreation"));

    public AccountMenu() {
        super(Translations.get("common:account"));

        setupButtonActions();

        addAll();
    }

    private void setupButtonActions() {
        openTab.setOnAction(e -> PotoFlux.app.setOpenedTab(Tabs.INSTANCE.ACCOUNT));
        auth.setOnAction(e -> performAuthAction());

        viewUsers.setOnAction(e -> Perms.VIEW_USERS.getPermAction().run());
        createUsers.setOnAction(e -> Perms.CREATE_USERS.getPermAction().run());
        deleteUsers.setOnAction(e -> Perms.DELETE_USERS.getPermAction().run());
        accountCreationLock.setOnAction(e -> ConnectionHandler.sendAccountCreationLockRequest(accountCreationLock.isSelected()));
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
        getItems().add(openTab);
        getItems().add(auth);

        // perms
        perms.getItems().add(viewUsers);
        perms.getItems().add(createUsers);
        perms.getItems().add(deleteUsers);
        perms.getItems().add(accountCreationLock);
        getItems().add(perms);
    }

}
