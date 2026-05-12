package net.minheur.potoflux.loader.mod;

public enum ModState {
    NOT_PROCESSED,
    LOADING,
    INCOMPATIBLE,
    CIRCULAR,
    MISSING_DEPENDENCIES,
    FAILED,
    circularLastest
}
