package net.minheur.potoflux.utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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
     * Gets a {@link List} of {@link String} from a {@link JsonObject} and it's member name
     * @param object the {@link JsonObject} to get the list from
     * @param listName the member name of the list in the JSON
     * @return the list of string contained in the object
     */
    public static List<String> listFromObject(JsonObject object, String listName) {

        Type t = new TypeToken<List<String>>(){}.getType();

        JsonElement jArray = object.get(listName);

        return GSON.fromJson(jArray, t);

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
