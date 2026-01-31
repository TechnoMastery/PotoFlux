package net.minheur.potoflux.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Json {
    private Json() {}

    public static final Gson GSON = new Gson();

    public static List<String> loadStringArray(URL url) throws Exception {
        try (InputStreamReader reader =
                new InputStreamReader(url.openStream(), StandardCharsets.UTF_8)) {

            Type type = new TypeToken<List<String>>(){}.getType();

            return GSON.fromJson(reader, type);
        }
    }

    public static String getFromObject(URL url, String key) throws Exception {
        try (InputStreamReader reader =
                     new InputStreamReader(url.openStream(), StandardCharsets.UTF_8)) {

            Type type = new TypeToken<List<String>>(){}.getType();

            JsonObject object = JsonParser.parseReader(reader).getAsJsonObject();

            return object.get(key).getAsString();
        }
    }
}
