package net.minheur.potoflux.loader.mod.errors;

import net.minheur.potoflux.loader.mod.Dependency;
import net.minheur.potoflux.loader.mod.Mod;

public class LoadModError {

    public final Mod mod;
    public Dependency missingDep;
    public String actualDepVersion;

    public LoadModError(Mod mod) {
        this.mod = mod;
    }

    public LoadModError(Mod mod, Dependency missingDep) {
        this.mod = mod;
        this.missingDep = missingDep;
    }
    public LoadModError(Mod mod, Dependency missingDep, String actualDepVersion) {
        this.mod = mod;
        this.missingDep = missingDep;
        this.actualDepVersion = actualDepVersion;
    }
}
