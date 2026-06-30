package net.minheur.potoflux.loader.mod.update;

import net.minheur.potoflux.loader.mod.Mod;
import net.minheur.potoflux.logger.LogCategories;
import net.minheur.potoflux.logger.PtfLogger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Mod update reg class.
 */
public class ModUpdateReg {
    /**
     * The update candidates list.
     */
    private static final List<Candidate> candidates = new ArrayList<>();
    /**
     * Weather the reg is opened
     */
    private static boolean opened = true;

    /**
     * Adds a candidate
     * @param mod the mod
     * @param compatible weather is compatible
     * @param declaredLastest declared lastest version of this mod
     */
    public static void add(Mod mod, boolean compatible, String declaredLastest) {
        add(new Candidate(mod, compatible, declaredLastest));
    }

    /**
     * Adds a mod
     * @param c the candidate
     */
    public static void add(Candidate c) {
        if (!opened) {
            PtfLogger.error("Can't add a mod to candidates after closing !", LogCategories.MOD_UPDATE, "candidates");
            return;
        }

        candidates.add(c);
    }

    /**
     * Closes the reg
     */
    public static void close() {
        if (opened) opened = false;
    }

    /**
     * Gets all candidates
     * @return copy of {@link #candidates}
     */
    public static @NotNull List<Candidate> getAll() {
        if (!opened) return List.copyOf(candidates);
        return new ArrayList<>();
    }
}
