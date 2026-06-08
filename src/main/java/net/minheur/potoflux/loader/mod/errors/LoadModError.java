package net.minheur.potoflux.loader.mod.errors;

import net.minheur.potoflux.loader.mod.Dependency;
import net.minheur.potoflux.loader.mod.Mod;
import net.minheur.potoflux.loader.mod.ModState;

public class LoadModError {

    public final Mod mod;
    public final ModState errorState;

    public Dependency missingDep;
    public String actualDepVersion;

    public LoadModError(Mod mod, ModState errorState) {
        this.mod = mod;
        this.errorState = errorState;
    }

    public LoadModError(Mod mod, ModState errorState, Dependency missingDep) {
        this.mod = mod;
        this.errorState = errorState;
        this.missingDep = missingDep;
    }
    public LoadModError(Mod mod, ModState errorState, Dependency missingDep, String actualDepVersion) {
        this.mod = mod;
        this.errorState = errorState;
        this.missingDep = missingDep;
        this.actualDepVersion = actualDepVersion;
    }
}
