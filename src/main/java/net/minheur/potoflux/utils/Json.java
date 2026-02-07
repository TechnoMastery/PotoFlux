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

        if (content == null) return null;

        Type type = new TypeToken<List<String>>(){}.getType();
        return GSON.fromJson(content, type);
    }

    /**
     * Gets a JSON object from an online file
     * @param url the path to file
     * @return the JSON object of the file
     */
    public static JsonObject getOnlineJsonObject(String url) {
        String content = OnlineReader.read(url);

        if (content == null) return null;

        return JsonParser.parseString(content).getAsJsonObject();
    }

    /**
     * Gets a string item from an online JSON object
     * @param url the link to your online JSON file containing your JSON object
     * @param key the key to the member of your object
     * @return the content of the member {@code key} in your online JSON object
     */
    public static String getFromObject(String url, String key) {
        String content = OnlineReader.read(url);

        if (content == null) return null;

        JsonObject object = JsonParser.parseString(content).getAsJsonObject();
        return object.get(key).getAsString();

    }
}
