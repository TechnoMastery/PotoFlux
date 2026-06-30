package net.minheur.potoflux.loader.mod;

/**
 * Mod state enum.
 */
public enum ModState {
    /**
     * This mod hasn't been loaded yet.
     */
    NOT_PROCESSED,
    /**
     * This mod is actually loading
     */
    LOADING,
    /**
     * This mod is incompatible with actual Potoflux
     */
    INCOMPATIBLE,
    /**
     * This mod is part of circular dependency
     */
    CIRCULAR,
    /**
     * This mod is missing one or more of its dependencies
     */
    MISSING_DEPENDENCIES,
    /**
     * One or more of this mod's dependencies are in the wrong version
     */
    DEPENDENCY_WRONG_VERSION,
    /**
     * This mod failed to load, without specification.
     */
    FAILED,
    /**
     * This mod has been loaded
     */
    LOADED,
    /**
     * This mod is causing circular.<br>
     * This is a temporary state.
     */
    circularLastest
}
