package net.minheur.potoflux.utils

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken

/**
 * Class to get a [Gson].
 * This allows to have less as possible in the project.
 */
object Json {

    /**
     * Use this to do your simple JSON commands.
     */
    @JvmField
    val GSON: Gson = Gson()

    @JvmStatic
    fun loadStringArray(url: String): List<String>? {
        val content = OnlineReader.read(url) ?: return null
        val type = object : TypeToken<List<String>>() {}.type
        return GSON.fromJson(content, type)
    }

    /**
     * Gets a [List] of [String] from a [JsonObject] and its member name
     * @param obj the [JsonObject] to get the list from
     * @param listName the member name of the list in the JSON
     * @return the list of string contained in the object
     */
    @JvmStatic
    fun listFromObject(obj: JsonObject, listName: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        val jArray = obj[listName]
        return GSON.fromJson(jArray, type)
    }

    /**
     * Gets a JSON object from an online file
     * @param url the path to file
     * @return the JSON object of the file
     */
    @JvmStatic
    fun getOnlineJsonObject(url: String): JsonObject? {
        val content = OnlineReader.read(url) ?: return null
        return JsonParser.parseString(content).asJsonObject
    }

    /**
     * Gets a string item from an online JSON object
     * @param url the link to your online JSON file containing your JSON object
     * @param key the key to the member of your object
     * @return the content of the member `key` in your online JSON object
     */
    @JvmStatic
    fun getFromObject(url: String, key: String): String? {
        val content = OnlineReader.read(url) ?: return null
        val obj = JsonParser.parseString(content).asJsonObject
        return obj[key].asString
    }
}
