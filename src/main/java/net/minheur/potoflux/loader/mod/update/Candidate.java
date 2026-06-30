package net.minheur.potoflux.loader.mod.update;

import net.minheur.potoflux.loader.mod.Mod;

/**
 * Candidate record, for a mod that may be loaded
 */
public record Candidate(Mod mod, boolean compatible, String declaredLastest) {
}
