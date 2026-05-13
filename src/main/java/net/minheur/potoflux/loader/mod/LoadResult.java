package net.minheur.potoflux.loader.mod;

public enum LoadResult {
    ALREADY_LOADED,
    ALREADY_FAILED,
    ALREADY_CIRCULAR,
    CIRCULAR,
    INCOMPATIBLE,
    COMPATIBLE_FAILED,
    DEPENDENCY_FAILED,
    LOADED
}
