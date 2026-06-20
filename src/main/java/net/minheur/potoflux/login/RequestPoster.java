package net.minheur.potoflux.login;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import net.minheur.potoflux.logger.LogCategories;
import net.minheur.potoflux.logger.PtfLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.UUID;

/**
 * This posts all requests to the SQL database.
 */
public class RequestPoster {

    /**
     * Address of the database's functions.<br>
     * Just need to be appended with the function's name
     */
    private static final String address = "https://rkujwtknzfbyocjrrpbi.supabase.co/rest/v1/rpc/";
    /**
     * Public anon key of the database
     */
    private static final String anonKey =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJrdWp3dGtuemZieW9janJycGJpIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzQzNTM1MDQsImV4cCI6MjA4OTkyOTUwNH0.XwIF4tfLaMLz2FUVKdSOyDbxLeaT9YOeePsbDyeNy8o";

    /**
     * Sends a direct post to the database, to the {@linkplain #address}. Uses the {@linkplain #anonKey} to get authorized connection.
     * @param message this get sent to the database as parameters.
     * @param method name of the database's function
     * @return the result sent by the database
     * @throws IOException if the database couldn't be reached
     */
    private static String get(String message, String method) throws IOException {

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(address + method))
                .timeout(Duration.ofSeconds(10))
                .header("apikey", anonKey)
                .header("Authorization", "Bearer " + anonKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(message, StandardCharsets.UTF_8))
                .build();

        try {
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException(e);
        }

    }

    /**
     * Checks if the given {@link String} given is a correct {@code UUID} that can be used as a token
     * @param token string that should be a token as UUID
     * @throws InvalidTokenException if the token isn't correct
     */
    private static void checkTokenFormat(String token) throws InvalidTokenException {
        if (token == null) {
            throw new InvalidTokenException("Token is null");
        }

        if (token.isEmpty()) {
            throw new InvalidTokenException("Token is empty");
        }

        try {
            UUID.fromString(token);
        } catch (IllegalArgumentException e) {
            throw new InvalidTokenException("Token is not a valid UUID", e);
        }
    }

    /**
     * Quickly gets a built JSON object containing the given token, ready to be sent to the database
     * @param token the token to put in the JSON
     * @return a built {@link JsonObject} with the token
     */
    private static String getFormatForToken(String token) {
        JsonObject obj = new JsonObject();
        obj.addProperty("p_token", token);
        return obj.toString();
    }

    /**
     * Asks for a token, from an email and a password
     * @param email id of the account
     * @param password password associated
     * @return the database's response
     * @throws IOException if the server couldn't be reached
     */
    public static String login(String email, String password) throws IOException {

        JsonObject obj = new JsonObject();
        obj.addProperty("p_email", email);
        obj.addProperty("p_password", password);

        return get(obj.toString(), "login");
    }

    /**
     * Checks if a token exists
     * @param token token to check
     * @return the database's response
     * @throws IOException if the server couldn't be reached
     * @throws InvalidTokenException if the token has invalid format
     */
    public static String checkToken(String token) throws IOException, InvalidTokenException {
        checkTokenFormat(token);
        String json = getFormatForToken(token);
        return get(json, "check_token");
    }

    /**
     * Get infos about your own account with your token
     * @param token associated with your account
     * @return the database's response
     * @throws IOException if the server couldn't be reached
     * @throws InvalidTokenException if the token has invalid format
     */
    public static String getInfos(String token) throws IOException, InvalidTokenException {
        checkTokenFormat(token);
        String json = getFormatForToken(token);
        return get(json, "get_infos");
    }

    /**
     * Creation of a user by an admin
     * @param token the admin's token
     * @param email of the new user
     * @param password of the new user
     * @param firstName of the new user
     * @param lastName of the new user
     * @param perms of the new user
     * @param rank of the new user
     * @return the database's response
     * @throws IOException if the server couldn't be reached
     * @throws InvalidTokenException if the admin's token has invalid format
     */
    public static String addUser(
            String token,
            String email,
            String password,
            String firstName,
            String lastName,
            String @NotNull [] perms,
            int rank
    ) throws InvalidTokenException, IOException {
        checkTokenFormat(token);

        JsonObject obj = new JsonObject();

        obj.addProperty("p_token", token);
        obj.addProperty("p_email", email);
        obj.addProperty("p_password", password);
        obj.addProperty("p_first_name", firstName);
        obj.addProperty("p_last_name", lastName);

        JsonArray jPerms = new JsonArray();
        for (String p : perms) jPerms.add(p);

        obj.add("p_perms", jPerms);
        obj.addProperty("p_rank", rank);


        return get(obj.toString(), "add_user");
    }
    /**
     * Removal of an account by and admin
     * @param token of the admin
     * @param email of the targeted account
     * @return the database's response
     * @throws IOException if the server couldn't be reached
     * @throws InvalidTokenException if the admin's token has invalid format
     */
    public static String rmUser(String token, String email) throws InvalidTokenException, IOException {
        checkTokenFormat(token);

        JsonObject obj = new JsonObject();
        obj.addProperty("p_token", token);
        obj.addProperty("p_user", email);

        return get(obj.toString(), "delete_user");
    }
    /**
     * Account self-creation poster
     * @param email of the account
     * @param password for the account
     * @param firstName of the user
     * @param lastName of the user
     * @return the database's response
     * @throws IOException if the server couldn't be reached
     */
    public static String createAccount(
            String email,
            String password,
            String firstName,
            String lastName
    ) throws IOException {

        JsonObject obj = new JsonObject();
        obj.addProperty("p_email", email);
        obj.addProperty("p_password", password);
        obj.addProperty("p_first_name", firstName);
        obj.addProperty("p_last_name", lastName);

        return get(obj.toString(), "create_account");
    }

    /**
     * Lists all users UUIDs
     * @param token of the admin
     * @return the database's response
     * @throws IOException if the server couldn't be reached
     * @throws InvalidTokenException if the admin's token has invalid format
     */
    public static String listUsers(String token) throws InvalidTokenException, IOException {
        checkTokenFormat(token);

        JsonObject obj = new JsonObject();
        obj.addProperty("p_token", token);

        return get(obj.toString(), "list_users");
    }
    /**
     * Get infos for a specified user
     * @param token of the admin
     * @param userUuid of the account to get specific specs
     * @return the database's response
     * @throws IOException if the server couldn't be reached
     * @throws InvalidTokenException if the admin's token has invalid format
     */
    public static String getUserInfos(String token, String userUuid) throws InvalidTokenException, IOException {
        checkTokenFormat(token);

        JsonObject obj = new JsonObject();
        obj.addProperty("p_token", token);
        obj.addProperty("p_user", userUuid);

        return get(obj.toString(), "get_user_info");
    }
    /**
     * Modify an account
     * @param token of the admin
     * @param targetUuid to modify the account of
     * @param newEmail of the account, of {@code null} if unchanged
     * @param newFirstName of the account, of {@code null} if unchanged
     * @param newLastName of the account, of {@code null} if unchanged
     * @param newRank of the account, of {@code 0} if unchanged
     * @param perms of the account, of {@code null} if unchanged
     * @return the database's response
     * @throws IOException if the server couldn't be reached
     * @throws InvalidTokenException if the admin's token has invalid format
     */
    public static @Nullable String mdUserInfos(
            String token,
            String targetUuid,
            @Nullable String newEmail,
            @Nullable String newFirstName,
            @Nullable String newLastName,
            int newRank,
            @Nullable String[] perms
    ) throws InvalidTokenException, IOException {

        checkTokenFormat(token);
        if (targetUuid == null) return null;

        boolean emailChanged = newEmail != null;
        boolean fNameChanged = newFirstName != null;
        boolean lNameChanged = newLastName != null;
        boolean rankChanged = newRank <= 100 && newRank >0;
        boolean permsChanged = perms != null;

        JsonObject obj = new JsonObject();
        obj.addProperty("p_token", token);
        obj.addProperty("p_user_id", targetUuid);

        // email
        if (emailChanged) obj.addProperty("p_email", newEmail);
        else obj.add("p_email", JsonNull.INSTANCE);

        // first name
        if (fNameChanged) obj.addProperty("p_first_name", newFirstName);
        else obj.add("p_first_name", JsonNull.INSTANCE);

        // last name
        if (lNameChanged) obj.addProperty("p_last_name", newLastName);
        else obj.add("p_last_name", JsonNull.INSTANCE);

        // rank
        obj.addProperty("p_rank",
                rankChanged ? newRank : 0
        );

        if (permsChanged) {
            JsonArray jPerms = new JsonArray();
            for (String p : perms) jPerms.add(p);

            obj.add("p_perms", jPerms);
        } else obj.add("p_perms", JsonNull.INSTANCE);

        return get(obj.toString(), "md_user_infos");

    }
    /**
     * Modify an account's password
     * @param token of the admin
     * @param targetUuid to change the password of
     * @param newPassword for the account
     * @return the database's response
     * @throws IOException if the server couldn't be reached
     * @throws InvalidTokenException if the admin's token has invalid format
     */
    public static @Nullable String mdUserPassword(
            String token,
            String targetUuid,
            String newPassword
    ) throws InvalidTokenException, IOException {
        checkTokenFormat(token);
        if (targetUuid == null) return null;

        JsonObject obj = new JsonObject();
        obj.addProperty("p_token", token);
        obj.addProperty("p_user_id", targetUuid);
        obj.addProperty("p_new_password", newPassword);

        return get(obj.toString(), "md_user_password");
    }
    /**
     * Lock or unlock a user's account
     * @param token of the admin
     * @param targetUuid that will be locked or unlocked
     * @param newState weather the account will now be locked or not
     * @return the database's response
     * @throws IOException if the server couldn't be reached
     * @throws InvalidTokenException if the admin's token has invalid format
     */
    public static @Nullable String lockUser(
            String token,
            String targetUuid,
            boolean newState
    ) throws InvalidTokenException, IOException {
        checkTokenFormat(token);
        if (targetUuid == null) return null;

        JsonObject obj = new JsonObject();
        obj.addProperty("p_token", token);
        obj.addProperty("p_target_id", targetUuid);
        obj.addProperty("p_is_locked", newState);

        return get(obj.toString(), "lock_account");
    }

    /**
     * Disable or enable account self-creation
     * @param token of the admin
     * @param newState weather the account self-creation is now allowed or not
     * @return the database's response
     * @throws IOException if the server couldn't be reached
     * @throws InvalidTokenException if the admin's token has invalid format
     */
    public static String lockAccountCreation(
            String token,
            boolean newState
    ) throws InvalidTokenException, IOException {
        checkTokenFormat(token);

        JsonObject obj = new JsonObject();
        obj.addProperty("p_token", token);
        obj.addProperty("p_new_state", newState);

        return get(obj.toString(), "account_creation_state");
    }

    /**
     * Remove a token from the database active tokens
     * @param token to be removed
     * @throws IOException if the server couldn't be reached
     */
    public static void rmToken(String token) throws IOException {
        try {
            checkTokenFormat(token);
        } catch (InvalidTokenException ignored) {
            PtfLogger.warning("Removing incorrect token...", LogCategories.TOKEN);
            return;
        }

        String json = getFormatForToken(token);
        get(json, "logout");
    }

    public static String getNotifications(String token) throws IOException, InvalidTokenException {
        checkTokenFormat(token);

        String json = getFormatForToken(token);
        return get(json, "get_notifications");
    }

    /**
     * Gets if the account self-creation is allowed
     * @return the database's response
     * @throws IOException if the server couldn't be reached
     */
    public static String isAccountCreationEnabled() throws IOException {
        return get("{}", "is_account_creation_enabled");
    }

    public static void warmupTls() {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://rkujwtknzfbyocjrrpbi.supabase.co"))
                    .timeout(Duration.ofSeconds(3))
                    .GET()
                    .build();
            client.send(request, HttpResponse.BodyHandlers.discarding());
        } catch (Exception ignored) {}
    }
}
