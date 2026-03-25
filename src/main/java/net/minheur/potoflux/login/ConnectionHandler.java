package net.minheur.potoflux.login;

import net.minheur.potoflux.logger.LogCategories;
import net.minheur.potoflux.logger.PtfLogger;
import net.minheur.potoflux.login.response.InfoResponse;
import net.minheur.potoflux.login.response.LoginResponse;
import net.minheur.potoflux.utils.Json;

import javax.annotation.Nullable;
import java.io.IOException;

public class ConnectionHandler {
    public static Account account;
    public static boolean isLogged = false;

    public static void logWith(String email, String password) {
        checkAndRemoveExistingToken();

        String token = getToken(email, password);
        if (token == null) return;

        accountFor(token);
        if (account == null) return;

        PtfLogger.info("Logged in as " + account.email, LogCategories.ACCOUNT);

    }

    private static void accountFor(String token) {
        String response;
        try {
            response = ConnectionPost.getInfos(token);
        } catch (InvalidTokenException e) {
            e.printStackTrace();
            PtfLogger.error("Token malformed !", LogCategories.ACCOUNT);
            return;
        } catch (IOException e) {
            e.printStackTrace();
            PtfLogger.error("Could not get response !", LogCategories.CONNEXION_POST);
            return;
        }

        InfoResponse infoResponse = Json.GSON.fromJson(response, InfoResponse.class);

        if (!infoResponse.success) {
            PtfLogger.error("Could not get user info: " + infoResponse.error, LogCategories.CONNEXION_POST);
            return;
        }

        account = new Account();
        account.uuid = infoResponse.uuid;
        account.email = infoResponse.email;
        account.firstName = infoResponse.firstName;
        account.lastName = infoResponse.lastName;
        account.perms = infoResponse.perms;

        isLogged = true;

        PtfLogger.info("Logged in as " + account.email, LogCategories.ACCOUNT);
        PtfLogger.info("User " + account.email + " has UUID: " + account.uuid, LogCategories.ACCOUNT_IDS);

        TokenHandler.save(token);
    }

    @Nullable
    public static String getToken(String email, String password) {
        String response;
        try {
            response = ConnectionPost.login(email, password);
        } catch (IOException e) {
            e.printStackTrace();
            PtfLogger.error("Could not connect !", LogCategories.CONNEXION_POST);
            return null;
        }

        LoginResponse loginResponse = Json.GSON.fromJson(response, LoginResponse.class);

        if (!loginResponse.success) {
            PtfLogger.error("Failed to connect: " + loginResponse.error, LogCategories.ACCOUNT_IDS);
            return null;
        }

        String token = loginResponse.token;
        if (token == null) {
            PtfLogger.error("No token received !", LogCategories.CONNEXION_POST);
            return null;
        }
        return token;
    }

     public static void checkAndRemoveExistingToken() {
        if (TokenHandler.has()) {
            PtfLogger.warning("Connecting while already having a token ! Removing...", LogCategories.ACCOUNT);
            try {
                ConnectionPost.rmToken(TokenHandler.get());
            } catch (IOException e) {
                e.printStackTrace();
                PtfLogger.error("Failed to remove old token", LogCategories.CONNEXION_POST);
            } catch (InvalidTokenException e) {
                e.printStackTrace();
                PtfLogger.warning("Previously saved token is invalid", LogCategories.ACCOUNT);
            }
            TokenHandler.clear();
        }
     }

     @Nullable
     public static Account getAccount() {
        return account;
     }
     public static void logout() {
         PtfLogger.info("Disconnection...", LogCategories.ACCOUNT);

         TokenHandler.rmOnlineToken();
         TokenHandler.clear();

         account = null;
         isLogged = false;

         PtfLogger.info("Disconnected !", LogCategories.ACCOUNT);
     }

}
