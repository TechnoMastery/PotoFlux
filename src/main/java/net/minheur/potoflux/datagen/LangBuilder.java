package net.minheur.potoflux.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minheur.potoflux.translations.Lang;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class LangBuilder {
    private final Map<Lang, Map<String, String>> entries = new HashMap<>();

    public LangBuilder() {
        for (Lang l : Lang.values()) entries.put(l, new HashMap<>());
    }

    public TranslationBuilder add(String key) {
        return new TranslationBuilder(key);
    }

    public void save(Path output) {
        try {
            Files.createDirectories(output);
        } catch (IOException e) {
            throw new RuntimeException("Could not create lang directory", e);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        for (Map.Entry<Lang, Map<String, String>> entry : entries.entrySet()) {
            try {
                Files.writeString(output.resolve(entry.getKey().code + ".json"), gson.toJson(entry.getValue()));
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("ERROR while generating lang: " + entry.getKey().code);
            }
        }
    }

    public class TranslationBuilder {
        private final String key;

        private TranslationBuilder(String key) {
            this.key = key;
        }

        public TranslationBuilder lang(Lang lang, String value) {
            Map<String, String> translations = entries.get(lang);
            if (translations.put(key, value) != null) throw new IllegalArgumentException("Duplicate translation key " + key + " for lang " + lang.code);
            return this;
        }

        public TranslationBuilder en(String value) {
            return lang(Lang.EN, value);
        }
        public TranslationBuilder fr(String value) {
            return lang(Lang.FR, value);
        }
    }
}
