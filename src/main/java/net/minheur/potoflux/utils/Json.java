package net.minheur.potoflux.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Class to get a [Gson].
 * This allows to have less as possible in the project.
 */
public final class Json {
    /**
     * Use this to do your simple JSON commands.
     */
    public static final Gson GSON = new Gson();

    /**
     * Locks class's instantiation
     */
    private Json() {}

    /**
     * Loads a string array stored as JSON in a specific URL
     * @param url the url to get JSON array from
     * @return the loaded string list
     */
    public static List<String> loadStringArray(String url) {
        String content = OnlineReader.read(url);
        if (content == null) return null;
        Type type = new TypeToken<List<String>>() {}.getType();
        return GSON.fromJson(content, type);
    }

    /**
     * Gets a {@link List} of {@link String} from a {@link JsonObject} and its member name
     *
     * @param obj      the [JsonObject] to get the list from
     * @param listName the member name of the list in the JSON
     * @return the list of string contained in the object
     */
    public static List<String> listFromObject(JsonObject obj, String listName) {
        Type type = new TypeToken<List<String>>() {}.getType();
        JsonElement jArray = obj.get(listName);
        return GSON.fromJson(jArray, type);
    }

    /**
     * Gets a JSON object from an online file
     *
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
     *
     * @param url the link to your online JSON file containing your JSON object
     * @param key the key to the member of your object
     * @return the content of the member `key` in your online JSON object
     */
    public static JsonElement getFromObject(String url, String key) {
        String content = OnlineReader.read(url);
        if (content == null) return null;
        JsonObject obj = JsonParser.parseString(content).getAsJsonObject();
        return obj.get(key);
    }
}
