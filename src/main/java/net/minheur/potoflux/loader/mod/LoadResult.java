package net.minheur.potoflux.loader.mod;

/**
 * Load result enum.<br>
 * Mods uses them to return their status on load request.
 */
public enum LoadResult {
    /**
     * Mod failed to load.<br>
     * No more info could be provided.
     */
    FAILED,
    /**
     * Mod was part of a circular dependency loop (failed)
     */
    ALREADY_CIRCULAR,
    /**
     * Mod is actually circular.<br>
     * The mod calling this as dependency is also part of it.
     */
    CIRCULAR,
    /**
     * Mod is incompatible with the actual Potoflux version (failure)
     */
    INCOMPATIBLE,
    /**
     * Mod failed to load its dependencies (failure)
     */
    DEPENDENCY_FAILED,
    /**
     * Mod has been loaded, or it has already been earlier. (pass)
     */
    LOADED
}
