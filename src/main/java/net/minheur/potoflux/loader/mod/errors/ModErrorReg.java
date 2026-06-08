package net.minheur.potoflux.loader.mod.errors;

import net.minheur.potoflux.loader.mod.Mod;

import java.util.ArrayList;
import java.util.List;

public class ModErrorReg {
    private static final List<LoadModError> errors = new ArrayList<>();
    private static boolean opened = true;

    public static void add(Mod mod) {
        add(new LoadModError(mod));
    }
    public static void add(LoadModError error) {
        if (!opened) throw new IllegalStateException("Can't add mod loading error candidates after closing !");
        errors.add(error);
    }

    public static void close() {
        if (opened) opened = false;
    }

    public static List<LoadModError> getAll() {
        if (!opened) return List.copyOf(errors);
        return new ArrayList<>();
    }
}
