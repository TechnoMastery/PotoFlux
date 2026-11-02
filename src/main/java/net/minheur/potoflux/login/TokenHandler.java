package net.minheur.potoflux.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.prefs.Preferences;

/**
 * Saves, stores and retrieves connection token
 */
public final class TokenHandler {
    private static final Logger log =
            LoggerFactory.getLogger("account/token");
    private static final Logger logRemote =
            LoggerFactory.getLogger("account/remote");

    /**
     * The prefs to get the token.
     */
    private static final Preferences prefs = Preferences.userNodeForPackage(TokenHandler.class);
    /**
     * The key for the token.
     */
    private static final String KEY = "token";

    /**
     * Locks class instantiation
     */
    private TokenHandler() {}

    /**
     * Saves the token
     * @param token the token
     */
    public static void save(String token) {
        if (token == null || token.isEmpty()) return;
        prefs.put(KEY, token);
        log.info("Saved token !");
    }

    /**
     * Gets the token
     * @return the saved token
     */
    public static String get() {
        return prefs.get(KEY, null);
    }

    /**
     * Clears the token
     */
    public static void clear() {
        prefs.remove(KEY);
        log.info("Cleared local token !");
    }

    /**
     * Checks if the token exists
     * @return if there is a token
     */
    public static boolean has() {
        String token = get();
        return token != null && !token.isEmpty();
    }

    /**
     * Clear online token
     */
    public static void rmOnlineToken() {
        try {
            RequestPoster.rmToken(get());
        } catch (IOException e) {
            logRemote.error("Failed to remove remote token", e);
        }
    }
}
