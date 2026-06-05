package net.minheur.potoflux.login;

import net.minheur.potoflux.logger.LogCategories;
import net.minheur.potoflux.logger.PtfLogger;

import java.io.IOException;
import java.util.prefs.Preferences;

/**
 * This saves, stores and retrieve your connection token
 */
public class TokenHandler {
    /**
     * Prefs for this package.<br>
     * Used to store token
     */
    private static final Preferences prefs = Preferences.userNodeForPackage(TokenHandler.class);

    /**
     * Key used to get the token in the {@linkplain #prefs}
     */
    private static final String key = "token";

    /**
     * Saves the token in the prefs
     * @param token to save
     */
    public static void save(String token) {
        if (token == null || token.isEmpty())
            return;

        prefs.put(key, token);
        PtfLogger.info("Saved token !", LogCategories.TOKEN);
    }

    /**
     * Gets the actual stores token
     * @return the actual token, {@code null} if none
     */
    public static String get() {
        return prefs.get(key, null);
    }

    /**
     * Removes the local token
     */
    public static void clear() {
        prefs.remove(key);
        PtfLogger.info("Cleared local token !", LogCategories.TOKEN);
    }

    /**
     * Checks if there is a token stored
     * @return if there's a token
     */
    public static boolean has() {
        String token = get();
        return token != null && !token.isEmpty();
    }

    /**
     * Sends a request to remove the token stored locally from the server
     */
    public static void rmOnlineToken() {
        try {
            RequestPoster.rmToken(
                    TokenHandler.get()
            );
        } catch (IOException e) {
            e.printStackTrace();
            PtfLogger.error("Could not remove token", LogCategories.CONNEXION_POST);
        }
    }
}
