package net.minheur.potoflux.translations;

import com.google.gson.reflect.TypeToken;
import net.minheur.potoflux.utils.Json;

import javax.swing.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated(since = "6.4", forRemoval = true)
public class TranslationsOld {
    private static final Map<Lang, List<Map<String, String>>> translationSources = new HashMap<>();
    private static final List<Map<String, String>> loadedTranslations = new ArrayList<>();

    // core translations loading
    public static void load(Lang lang) {
        loadedTranslations.clear();
        loadedTranslations.addAll(translationSources.get(lang));
    }
    public static void load(String langCode) {
        for (Lang lang : Lang.values()) {
            if (lang.code.equals(langCode)) {
                load(lang);
                return;
            }
        }
        throw new IllegalArgumentException("Trying to use unsupported language !");
    }

    public static void registerAll() {
        prepareSource();

        ClassLoader cl = TranslationsOld.class.getClassLoader();

        for (Lang lang : Lang.values()) {
            try (InputStream is = cl.getResourceAsStream("lang/" + lang.code + ".json")) {
                if (is == null) throw new IllegalStateException("Missing translation file: lang/" + lang.code + ".json");

                try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                    Map<String, String> translations =
                            Json.GSON.fromJson(reader, new TypeToken<Map<String, String>>(){}.getType());
                    translationSources.get(lang).add(translations);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "ERROR: Failed loading translations '" + lang.code + "' ! Please report this error."
                );
                prepareSource();
            }
        }
    }

    public static void registerByStream(Lang lang, InputStream in) {
        try (Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
            Map<String, String> modTranslations = Json.GSON.fromJson(reader, new TypeToken<Map<String, String>>(){}.getType());
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
