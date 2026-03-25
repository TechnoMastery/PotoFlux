package net.minheur.potoflux.login;

import net.minheur.potoflux.logger.LogCategories;
import net.minheur.potoflux.logger.PtfLogger;

import java.io.IOException;
import java.util.prefs.Preferences;

public class TokenHandler {
    private static final Preferences prefs = Preferences.userNodeForPackage(TokenHandler.class);

    private static final String key = "token";

    public static void save(String token) {
        if (token == null || token.isEmpty())
            return;

        prefs.put(key, token);
        PtfLogger.info("Saved token !", LogCategories.TOKEN);
    }

    public static String get() {
        return prefs.get(key, null);
    }

    public static void clear() {
        prefs.remove(key);
        PtfLogger.info("Cleared token !", LogCategories.TOKEN);
    }

    public static boolean has() {
        String token = get();
        return token != null && !token.isEmpty();
    }

    public static void rmOnlineToken() {
        try {
            ConnectionPost.rmToken(
                    TokenHandler.get()
            );
        } catch (IOException e) {
            e.printStackTrace();
            PtfLogger.error("Could not remove token", LogCategories.CONNEXION_POST);
        }
    }
}
