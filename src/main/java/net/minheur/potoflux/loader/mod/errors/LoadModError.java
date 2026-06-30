package net.minheur.potoflux.loader.mod.errors;

import net.minheur.potoflux.loader.mod.Dependency;
import net.minheur.potoflux.loader.mod.Mod;
import net.minheur.potoflux.loader.mod.ModState;

/**
 * Load mod error class.<br>
 * Used in {@link ModErrorReg} to display mod loading errors
 */
public class LoadModError {

    /**
     * Mod that failed
     */
    public final Mod mod;
    /**
     * State of failure of the mod
     */
    public final ModState errorState;

    /**
     * Dependency that was missing.<br>
     * May not be used
     */
    public Dependency missingDep;
    /**
     * Actual version of the {@linkplain #missingDep}, if it is version compatibility issue
     */
    public String actualDepVersion;

    /**
     * Constructs a new LoadModError.
     */
    public LoadModError(Mod mod, ModState errorState) {
        this.mod = mod;
        this.errorState = errorState;
    }

    /**
     * Constructs a new LoadModError.
     */
    public LoadModError(Mod mod, ModState errorState, Dependency missingDep) {
        this.mod = mod;
        this.errorState = errorState;
        this.missingDep = missingDep;
    }

    /**
     * Constructs a new LoadModError.
     */
    public LoadModError(Mod mod, ModState errorState, Dependency missingDep, String actualDepVersion) {
        this.mod = mod;
        this.errorState = errorState;
        this.missingDep = missingDep;
        this.actualDepVersion = actualDepVersion;
    }
}
