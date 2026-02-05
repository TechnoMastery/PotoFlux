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

/**
 * Class to get a {@link Gson}.<br>
 * This allows to have less as possible in the project.
 */
public class Json {
    private Json() {}

    /**
     * Use this to do your simple JSON commands.
     */
    public static final Gson GSON = new Gson();

    public static List<String> loadStringArray(String url) {
        String content = OnlineReader.read(url);
        Type type = new TypeToken<List<String>>(){}.getType();
        return GSON.fromJson(content, type);
    }

    public static String getFromObject(String url, String key) throws Exception {
        String content = OnlineReader.read(url);
        JsonObject object = JsonParser.parseString(content).getAsJsonObject();
        return object.get(key).getAsString();

    }
}
