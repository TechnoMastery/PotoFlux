package net.minheur.potoflux.screen.menu.definers;

import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.login.ConnectionHandler;
import net.minheur.potoflux.login.perms.Perms;
import net.minheur.potoflux.screen.tabs.Tabs;
import net.minheur.potoflux.translations.Translations;

import java.util.Arrays;
import java.util.List;

import static net.minheur.potoflux.login.ConnectionHandler.*;

/**
 * The menu for the account shortcuts
 */
public class AccountMenu extends Menu {
    /**
     * Action to open the acocunt's tab
     */
    private final MenuItem openTab = new MenuItem(Translations.get("potoflux:menu.account.openTab"));
    /**
     * Performs {@link ConnectionHandler#performAuthAction()}
     */
    private final MenuItem auth = new MenuItem(getAuthButtonStatus());

    /**
     * Sub menu for all perm actions
     */
    private final Menu perms = new Menu(Translations.get("potoflux:menu.account.perms"));
    /**
     * Runs the perm to open the list of accounts
     */
    private final MenuItem viewUsers = new MenuItem(Translations.get("potoflux:perms.viewUsers"));
    /**
     * Runs the perm to create a user
     */
    private final MenuItem createUsers = new MenuItem(Translations.get("potoflux:menu.account.createUsers"));
    /**
     * Runs the perm to delete an account
     */
    private final MenuItem deleteUsers = new MenuItem(Translations.get("potoflux:menu.account.deleteUsers"));
    /**
     * Controls weather the account creation is allowed
     */
    private final CheckMenuItem accountCreationLock = new CheckMenuItem(Translations.get("potoflux:menu.account.lockAccountCreation"));

    /**
     * Creates a new account menu, adds all items and actions
     */
    public AccountMenu() {
        super(Translations.get("common:account"));

        setupButtonActions();

        addAll();
    }

    /**
     * Adds all actions to the different menu items
     */
    private void setupButtonActions() {
        openTab.setOnAction(e -> PotoFlux.app.setOpenedTab(Tabs.INSTANCE.ACCOUNT));
        auth.setOnAction(e -> performAuthAction());

        viewUsers.setOnAction(e -> Perms.VIEW_USERS.getPermAction().run());
        createUsers.setOnAction(e -> Perms.CREATE_USERS.getPermAction().run());
        deleteUsers.setOnAction(e -> Perms.DELETE_USERS.getPermAction().run());
        accountCreationLock.setOnAction(e -> ConnectionHandler.sendAccountCreationLockRequest(accountCreationLock.isSelected()));
    }

    /**
     * Reloads the menu, adding or removing perm and updating the allow account creation checkbox<br>
     * Ran on init ({@linkplain AccountMenu#AccountMenu()}) and on UI reload ({@linkplain ConnectionHandler#reloadAuthUi()})
     */
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
    /**
     * Adds all items to the menu.<br>
     * Also adds item to the perm's sub menu
     */
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
