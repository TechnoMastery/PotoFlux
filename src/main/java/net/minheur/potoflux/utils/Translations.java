package net.minheur.potoflux.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Translations {
    private static Map<String, String> translations = new HashMap<>();

    public static void load(String langCode) {
        try (Reader reader = new InputStreamReader(
                Translations.class.getResourceAsStream("/lang/" + langCode + ".json"),
                StandardCharsets.UTF_8
        )) {
            translations = new Gson().fromJson(reader, new TypeToken<Map<String, String>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "ERROR: Failed loading translations ! Please report this error.");
            translations = new HashMap<>();
        }
    }

    public static String get(String key) {
        return translations.getOrDefault(key, key);
    }
}
