package net.minheur.potoflux.loader.mod.update;

import net.minheur.potoflux.loader.mod.Mod;

public record Candidate(Mod mod, boolean compatible, String declaredLastest) {}
