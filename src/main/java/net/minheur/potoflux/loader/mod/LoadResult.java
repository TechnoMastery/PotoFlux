package net.minheur.potoflux.loader.mod;

public enum LoadResult {
    ALREADY_LOADED,
    ALREADY_FAILED,
    CIRCULAR,
    INCOMPATIBLE,
    DEPENDENCY_FAILED,
    CIRCULAR_CAUSE,
    LOADED
}
