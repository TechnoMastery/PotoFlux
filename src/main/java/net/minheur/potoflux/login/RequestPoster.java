package net.minheur.potoflux.login;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import net.minheur.potoflux.logger.LogCategories;
import net.minheur.potoflux.logger.PtfLogger;

import javax.annotation.CheckForNull;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

import static net.minheur.potoflux.Functions.formatMessage;

public class RequestPoster {

    private static final String address = "https://rkujwtknzfbyocjrrpbi.supabase.co/rest/v1/rpc/";
    private static final String anonKey =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJrdWp3dGtuemZieW9janJycGJpIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzQzNTM1MDQsImV4cCI6MjA4OTkyOTUwNH0.XwIF4tfLaMLz2FUVKdSOyDbxLeaT9YOeePsbDyeNy8o";

    private static String get(String message, String method) throws IOException {

        URL url = new URL(address + method);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("apikey", anonKey);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + anonKey);
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(message.getBytes());
        }

        InputStream is;

        if (conn.getResponseCode() >= 200 && conn.getResponseCode() < 300)
            is = conn.getInputStream();
        else
            is = conn.getErrorStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null)
            response.append(line);

        return response.toString();

    }

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

    private static String getFormatForToken(String token) {
        JsonObject obj = new JsonObject();
        obj.addProperty("p_token", token);
        return obj.toString();
    }

    public static String login(String email, String password) throws IOException {

        JsonObject obj = new JsonObject();
        obj.addProperty("p_email", email);
        obj.addProperty("p_password", password);

        return get(obj.toString(), "login");
    }

    public static String checkToken(String token) throws IOException, InvalidTokenException {
        checkTokenFormat(token);
        String json = getFormatForToken(token);
        return get(json, "check_token");
    }

    public static String getInfos(String token) throws IOException, InvalidTokenException {
        checkTokenFormat(token);
        String json = getFormatForToken(token);
        return get(json, "get_infos");
    }

    public static String addUser(
            String token,
            String email,
            String password,
            String firstName,
            String lastName,
            String[] perms,
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
    public static String rmUser(String token, String email) throws InvalidTokenException, IOException {
        checkTokenFormat(token);

        JsonObject obj = new JsonObject();
        obj.addProperty("p_token", token);
        obj.addProperty("p_user", email);

        return get(obj.toString(), "delete_user");
    }
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

    public static String listUsers(String token) throws InvalidTokenException, IOException {
        checkTokenFormat(token);

        JsonObject obj = new JsonObject();
        obj.addProperty("p_token", token);

        return get(obj.toString(), "list_users");
    }
    public static String getUserInfos(String token, String userUuid) throws InvalidTokenException, IOException {
        checkTokenFormat(token);

        JsonObject obj = new JsonObject();
        obj.addProperty("p_token", token);
        obj.addProperty("p_user", userUuid);

        return get(obj.toString(), "get_user_info");
    }

    public static String mdUserInfos(
            String token,
            String targetUuid,
            @CheckForNull String newEmail,
            @CheckForNull String newFirstName,
            @CheckForNull String newLastName,
            int newRank
    ) throws InvalidTokenException, IOException {

        checkTokenFormat(token);
        if (targetUuid == null) return null;

        boolean emailChanged = newEmail != null;
        boolean fNameChanged = newFirstName != null;
        boolean lNameChanged = newLastName != null;
        boolean rankChanged = newRank <= 100 && newRank >0;

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

        return get(obj.toString(), "md_user_infos");

    }
    public static String mdUserPassword(
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
    public static String lockUser(
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

}
