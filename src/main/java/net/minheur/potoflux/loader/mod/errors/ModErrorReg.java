package net.minheur.potoflux.loader.mod.errors;

import net.minheur.potoflux.loader.mod.Mod;
import net.minheur.potoflux.loader.mod.ModState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Mod error reg.<br>
 * Stores all mods that failed loading, to display their error on FX thread.
 */
public class ModErrorReg {
    /**
     * The errors list.
     */
    private static final List<LoadModError> errors = new ArrayList<>();
    /**
     * Weather th reg is opened
     */
    private static boolean opened = true;

    /**
     * Adds a mod to the reg
     * @param mod the mod
     * @param errorState the error state
     */
    public static void add(Mod mod, ModState errorState) {
        add(new LoadModError(mod, errorState));
    }

    /**
     * Adds a mod to the reg
     * @param error the mod loading error container
     */
    public static void add(LoadModError error) {
        if (!opened) throw new IllegalStateException("Can't add mod loading error candidates after closing !");
        errors.add(error);
    }

    /**
     * Closes the reg
     */
    public static void close() {
        if (opened) opened = false;
    }

    /**
     * Gets all errors
     * @return copy of {@link #errors}
     */
    public static @NotNull List<LoadModError> getAll() {
        if (!opened) return List.copyOf(errors);
        return new ArrayList<>();
    }
}
