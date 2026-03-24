package net.minheur.potoflux.login;

import java.util.prefs.Preferences;

public class TokenHandler {
    private static final Preferences prefs = Preferences.userNodeForPackage(TokenHandler.class);

    private static final String key = "token";

    public static void save(String token) {
        if (token == null || token.isEmpty())
            return;

        prefs.put(key, token);
    }

    public static String get() {
        return prefs.get(key, null);
    }

    public static void clear() {
        prefs.remove(key);
    }

    public static boolean has() {
        String token = get();
        return token != null && !token.isEmpty();
    }
}
