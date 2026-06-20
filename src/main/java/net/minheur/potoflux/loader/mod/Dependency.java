package net.minheur.potoflux.loader.mod;

/**
 * Represents a dependency with id and minimal/maximal versions
 */
public class Dependency {
    public final String id;
    public final String minVersion;
    public final String maxVersion;

    public Dependency(String id, String version) {
        this.id = id;
        this.minVersion = version;
        this.maxVersion = version;
    }

    public Dependency(String formatted) {
        String[] parts = formatted.split(":");
        this.id = parts[0];
        this.minVersion = parts[1];
        this.maxVersion = parts.length == 2 ? parts[1] : parts[2];
    }
}
