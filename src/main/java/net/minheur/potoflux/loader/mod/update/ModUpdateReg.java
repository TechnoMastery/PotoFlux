package net.minheur.potoflux.loader.mod.update;

import net.minheur.potoflux.loader.mod.Mod;
import net.minheur.potoflux.logger.LogCategories;
import net.minheur.potoflux.logger.PtfLogger;

import java.util.ArrayList;
import java.util.List;

public class ModUpdateReg {
    private static final List<Candidate> candidates = new ArrayList<>();
    private static boolean opened = true;

    public static void add(Mod mod, boolean compatible, String declaredLastest) {
        add(new Candidate(mod, compatible, declaredLastest));
    }
    public static void add(Candidate c) {
        if (!opened) {
            PtfLogger.error("Can't add a mod to candidates after closing !", LogCategories.MOD_UPDATE, "candidates");
            return;
        }

        candidates.add(c);
    }

    public static void close() {
        if (opened) opened = false;
    }

    public static List<Candidate> getAll() {
        if (!opened) return List.copyOf(candidates);
        return new ArrayList<>();
    }
}
