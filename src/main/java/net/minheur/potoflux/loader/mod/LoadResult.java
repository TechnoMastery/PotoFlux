package net.minheur.potoflux.loader.mod;

public enum LoadResult {
    FAILED,
    ALREADY_CIRCULAR,
    CIRCULAR,
    INCOMPATIBLE,
    DEPENDENCY_FAILED,
    LOADED
}
