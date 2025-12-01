package net.minheur.potoflux.translations;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Translations {
    private static final Map<Lang, List<Map<String, String>>> translationSources = new HashMap<>();
    private static final List<Map<String, String>> loadedTranslations = new ArrayList<>();

    // core translations loading
    public static void load(Lang lang) {
        loadedTranslations.addAll(translationSources.get(lang));
    }
    public static void load(String langCode) {
        for (Lang lang : Lang.values()) {
            if (lang.code.equals(langCode)) load(lang);
            return;
        }
        throw new IllegalArgumentException("Trying to use unsupported language !");
    }

    public static void registerAll() {
        prepareSource();

        for (Lang lang : Lang.values()) {
            try (Reader reader = new InputStreamReader(Translations.class.getResourceAsStream("/lang/" + lang.code + ".json"),
                    StandardCharsets.UTF_8
            )) {
                Map<String, String> translations = new Gson().fromJson(reader, new TypeToken<Map<String, String>>(){}.getType());
                translationSources.get(lang).add(translations);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "ERROR: Failed loading translations ! Please report this error.");
                prepareSource();
            }
        }
    }

    public static void registerByStream(Lang lang, InputStream in) {
        try (Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
            Map<String, String> modTranslations = new Gson().fromJson(reader, new TypeToken<Map<String, String>>(){}.getType());
            translationSources.get(lang).add(modTranslations);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // get the traductions looking threw all sources, priority : core
    public static String get(String key) {
        for (Map<String, String> source : loadedTranslations) if (source.containsKey(key)) return source.get(key);
        return key;
    }

    private static void prepareSource() {
        translationSources.clear();
        for (Lang lang : Lang.values()) translationSources.put(lang, new ArrayList<>());
    }
}
