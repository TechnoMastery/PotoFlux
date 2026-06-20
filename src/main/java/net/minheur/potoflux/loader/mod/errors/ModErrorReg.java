package net.minheur.potoflux.loader.mod.errors;

import net.minheur.potoflux.loader.mod.Mod;
import net.minheur.potoflux.loader.mod.ModState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ModErrorReg {
    private static final List<LoadModError> errors = new ArrayList<>();
    private static boolean opened = true;

    public static void add(Mod mod, ModState errorState) {
        add(new LoadModError(mod, errorState));
    }

    public static void add(LoadModError error) {
        if (!opened) throw new IllegalStateException("Can't add mod loading error candidates after closing !");
        errors.add(error);
    }

    public static void close() {
        if (opened) opened = false;
    }

    public static @NotNull List<LoadModError> getAll() {
        if (!opened) return List.copyOf(errors);
        return new ArrayList<>();
    }
}
