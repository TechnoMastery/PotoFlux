package net.minheur.potoflux.login;

import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.logger.LogCategories;
import net.minheur.potoflux.logger.PtfLogger;
import net.minheur.potoflux.login.perms.Perms;
import net.minheur.potoflux.login.response.InfoResponse;
import net.minheur.potoflux.login.response.IsAccountCreationEnabledResponse;
import net.minheur.potoflux.login.response.LoginResponse;
import net.minheur.potoflux.screen.menu.MenuContent;
import net.minheur.potoflux.screen.menu.definers.AccountMenu;
import net.minheur.potoflux.screen.tabs.Tabs;
import net.minheur.potoflux.screen.tabs.all.AccountTab;
import net.minheur.potoflux.translations.Translations;
import net.minheur.potoflux.utils.Json;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static net.minheur.potoflux.ui.UiUtils.showErrorPane;

/**
 * Handles all things related to connecting you to your account, disconnecting you, and keeping your {@link #account}<br>
 * It synchronizes multiple other classes to do so
 */
public class ConnectionHandler {
    public static Account account;
    public static boolean isLogged = false;
    public static boolean isAccountCreationEnabled = false;

    public static void logWith(String email, String password) {
        checkAndRemoveExistingToken();

        String token = getToken(email, password);
        if (token == null) return;

        accountFor(token);

    }

    public static void accountFor(String token) {
        String response;
        try {
            response = RequestPoster.getInfos(token);
        } catch (InvalidTokenException e) {
            e.printStackTrace();
            PtfLogger.error("Token malformed !", LogCategories.ACCOUNT);
            showErrorPane(Translations.get("potoflux:tabs.account.error.tokenMalformed"));
            return;
        } catch (IOException e) {
            e.printStackTrace();
            PtfLogger.error("Could not get response !", LogCategories.CONNEXION_POST);
            showErrorPane(Translations.get("potoflux:tabs.account.error.noResponse"));
            return;
        }

        InfoResponse infoResponse = Json.GSON.fromJson(response, InfoResponse.class);

        if (!infoResponse.success) {
            PtfLogger.error("Could not get user info: " + infoResponse.error, LogCategories.CONNEXION_POST);
            displayInfoError(infoResponse);
            return;
        }

        account = new Account();
        account.uuid = infoResponse.uuid;
        account.email = infoResponse.email;
        account.firstName = infoResponse.firstName;
        account.lastName = infoResponse.lastName;
        account.rank = infoResponse.rank;
        account.perms = fillPerms(infoResponse.perms);

        isLogged = true;

        PtfLogger.info("Logged in as " + account.email, LogCategories.ACCOUNT);
        PtfLogger.info("User " + account.email + " has UUID: " + account.uuid, LogCategories.ACCOUNT_IDS);

        TokenHandler.save(token);
    }

    public static Perms[] fillPerms(String[] perms) {
        List<Perms> newPerms = new ArrayList<>();

        for (String perm : perms) {
            Perms p = Perms.getFromCode(perm);
            if (p == null) continue;
            newPerms.add(p);
        }

        return newPerms.toArray(Perms[]::new);
    }

    private static void displayInfoError(InfoResponse infoResponse) {
        switch (infoResponse.error) {
            case "user_not_found" -> showErrorPane(Translations.get("potoflux:tabs.account.error.token.noUser"));
            case "not_exists" -> showErrorPane(Translations.get("potoflux:tabs.account.error.token.notExists"));
            case "token_expired" -> showErrorPane(Translations.get("potoflux:tabs.account.error.token.expired"));
            default -> showErrorPane(infoResponse.error);
        }
    }

    @Nullable
    public static String getToken(String email, String password) {
        String response;
        try {
            response = RequestPoster.login(email, password);
        } catch (IOException e) {
            e.printStackTrace();
            PtfLogger.error("Could not get response !", LogCategories.CONNEXION_POST);
            showErrorPane(Translations.get("potoflux:tabs.account.error.noResponse"));
            return null;
        }

        LoginResponse loginResponse = Json.GSON.fromJson(response, LoginResponse.class);

        if (!loginResponse.success) {
            PtfLogger.error("Failed to connect: " + loginResponse.error, LogCategories.ACCOUNT_IDS);
            displayLoggingError(loginResponse);
            return null;
        }

        String token = loginResponse.token;
        if (token == null) {
            PtfLogger.error("No token received !", LogCategories.CONNEXION_POST);
            showErrorPane(Translations.get("potoflux:tabs.account.error.noToken"));
            return null;
        }
        return token;
    }
    public static void reloadAccountCreationPermission() {
        String content;
        try {
            content = RequestPoster.isAccountCreationEnabled();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorPane(Translations.get("potoflux:tabs.account.failed"));
            return;
        }

        IsAccountCreationEnabledResponse response = Json.GSON.fromJson(content, IsAccountCreationEnabledResponse.class);
        isAccountCreationEnabled = response.isEnabled;
    }

    private static void displayLoggingError(LoginResponse loginResponse) {
        switch (loginResponse.error) {
            case "user_not_found" -> showErrorPane(Translations.get("potoflux:tabs.account.error.noUser"));
            case "invalid_password" -> showErrorPane(Translations.get("potoflux:tabs.account.error.invalidPassword"));
            default -> showErrorPane(loginResponse.error);
        }
    }

    public static void checkAndRemoveExistingToken() {
       if (TokenHandler.has()) {
           PtfLogger.warning("Connecting while already having a token ! Removing...", LogCategories.ACCOUNT);
           try {
               RequestPoster.rmToken(TokenHandler.get());
           } catch (IOException e) {
               e.printStackTrace();
               PtfLogger.error("Failed to remove old token", LogCategories.CONNEXION_POST);
           }
           TokenHandler.clear();
       }
    }

    public static void performAuthAction() {
        if (isLogged) logout();
        else login();
        reloadAuthUi();
    }
    public static void reloadAuthUi() {

        ((AccountTab) PotoFlux.app.getTabMap().get(Tabs.INSTANCE.ACCOUNT)).reload();

        ((AccountMenu) MenuContent.INSTANCE.ACCOUNT.content()).reload();
    }
    public static void logout() {
       if (!isLogged) return;

       PtfLogger.info("Disconnection...", LogCategories.ACCOUNT);

       TokenHandler.rmOnlineToken();
       TokenHandler.clear();

       account = null;
       isLogged = false;

       PtfLogger.info("Disconnected !", LogCategories.ACCOUNT);
    }

    public static void login() {
       PtfLogger.info("Logging in...", LogCategories.ACCOUNT);

       JDialog dialog = new JDialog(PotoFlux.app.getFrame(), Translations.get("common:connection"), true);
       dialog.setSize(450, 150);
       dialog.setLocationRelativeTo(null);
       dialog.setLayout(new BorderLayout());

       // FIELDS

       JPanel fieldsPanel = new JPanel();
       fieldsPanel.setLayout(new GridLayout(2, 2, 5, 5));
       fieldsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

       JLabel emailLabel = new JLabel(Translations.get("common:emailField"));
       JTextField emailField = new JTextField();

       JLabel passwordLabel = new JLabel(Translations.get("common:passwordField"));
       JPasswordField passwordField = new JPasswordField();

       fieldsPanel.add(emailLabel);
       fieldsPanel.add(emailField);
       fieldsPanel.add(passwordLabel);
       fieldsPanel.add(passwordField);

       // BUTTONS

       JPanel buttonsPanel = new JPanel();
       buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

       JButton cancelButton = new JButton(Translations.get("common:cancel"));
       JButton loginButton = new JButton(Translations.get("common:connection"));

       buttonsPanel.add(cancelButton);
       buttonsPanel.add(loginButton);

       // ACTIONS

       cancelButton.addActionListener(e -> {
           dialog.dispose();
           PtfLogger.info("Connection canceled.", LogCategories.ACCOUNT);
       });

       loginButton.addActionListener(e -> {
           String email = emailField.getText().trim().toLowerCase();
           String password = new String(passwordField.getPassword()).trim();

           logout();
           logWith(email, password);

           dialog.dispose();
       });

       dialog.getRootPane().setDefaultButton(loginButton);

       dialog.add(fieldsPanel, BorderLayout.CENTER);
       dialog.add(buttonsPanel, BorderLayout.SOUTH);

       dialog.setVisible(true);
    }
    public static String getAuthButtonStatus() {
        return isLogged ?
                Translations.get("common:disconnect") :
                Translations.get("common:connect");
    }

}
