package net.minheur.potoflux.loader.mod;

public enum ModState {
    NOT_PROCESSED,
    LOADING,
    INCOMPATIBLE,
    CIRCULAR,
    MISSING_DEPENDENCIES,
    DEPENDENCY_WRONG_VERSION,
    FAILED,
    LOADED,
    circularLastest
}
