package net.minheur.potoflux.login;

import net.minheur.potoflux.logger.LogCategories;
import net.minheur.potoflux.logger.PtfLogger;
import net.minheur.potoflux.login.perms.Perms;
import net.minheur.potoflux.login.response.InfoResponse;
import net.minheur.potoflux.login.response.LoginResponse;
import net.minheur.potoflux.translations.Translations;
import net.minheur.potoflux.utils.Json;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static net.minheur.potoflux.ui.UiUtils.showErrorPane;

public class ConnectionHandler {
    public static Account account;
    public static boolean isLogged = false;

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
        fillPerms(infoResponse.perms);

        isLogged = true;

        PtfLogger.info("Logged in as " + account.email, LogCategories.ACCOUNT);
        PtfLogger.info("User " + account.email + " has UUID: " + account.uuid, LogCategories.ACCOUNT_IDS);

        TokenHandler.save(token);
    }

    private static void fillPerms(String[] perms) {
        List<Perms> newPerms = new ArrayList<>();

        for (String perm : perms) {
            Perms p = Perms.getFromCode(perm);
            if (p == null) continue;
            newPerms.add(p);
        }

        account.perms = newPerms.toArray(Perms[]::new);
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

     public static void logout() {
        if (!isLogged) return;

        PtfLogger.info("Disconnection...", LogCategories.ACCOUNT);

         TokenHandler.rmOnlineToken();
         TokenHandler.clear();

         account = null;
         isLogged = false;

         PtfLogger.info("Disconnected !", LogCategories.ACCOUNT);
     }

}
