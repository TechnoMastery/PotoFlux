package net.minheur.potoflux.utils;

import com.google.gson.Gson;

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
}
