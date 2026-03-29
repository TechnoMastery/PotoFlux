package net.minheur.potoflux.login;

import net.minheur.potoflux.logger.LogCategories;
import net.minheur.potoflux.logger.PtfLogger;

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
        return formatMessage(
                """
                        {
                            "p_token": "$$1"
                        }
                        """,
                token
        );
    }

    public static String login(String email, String password) throws IOException {
        String json = formatMessage(
                """
                        {
                            "p_email": "$$1",
                            "p_password": "$$2"
                        }
                        """,
                email, password
        );

        return get(json, "login");
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
        String json = formatMessage(
                """
                        {
                            "p_token": "$$1",
                            "p_email": "$$2",
                            "p_password": "$$3",
                            "p_first_name": "$$4",
                            "p_last_name": "$$5",
                            "p_perms": $$6,
                            "p_rank": $$7
                        }
                        """,
                token, email, password, firstName, lastName,
                Arrays.stream(perms)
                        .map(s -> "\"" + s + "\"")
                        .collect(Collectors.joining(",", "[", "]")),
                rank
        );

        return get(json, "add_user");
    }
    public static String rmUser(String token, String email) throws InvalidTokenException, IOException {
        checkTokenFormat(token);
        String json = formatMessage(
                """
                        {
                            "p_token": "$$1",
                            "p_user": "$$2"
                        }
                        """,
                token, email
        );

        return get(json, "delete_user");
    }

    public static String listUsers(String token) throws InvalidTokenException, IOException {
        checkTokenFormat(token);
        String json = formatMessage(
                """
                        {
                            "p_token": "$$1"
                        }
                        """,
                token
        );

        return get(json, "list_users");
    }
    public static String getUserInfos(String token, String userUuid) throws InvalidTokenException, IOException {
        checkTokenFormat(token);
        String json = formatMessage(
                """
                        {
                            "p_token": "$$1",
                            "p_user": "$$2"
                        }
                        """,
                token, userUuid
        );

        return get(json, "get_user_info");
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
