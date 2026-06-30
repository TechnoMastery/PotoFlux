package net.minheur.potoflux.loader.mod;

/**
 * Represents a dependency with id and minimal/maximal versions
 */
public class Dependency {
    /**
     * The id field.
     */
    public final String id;
    /**
     * The minimum required version field.
     */
    public final String minVersion;
    /**
     * The maximum allowed version field.
     */
    public final String maxVersion;

    /**
     * Constructs a new Dependency, with separated ID and version.<br>
     * Version is min and max at the same time.
     * @param id ID of the dependency mod
     * @param version Version of the dependency mod
     */
    public Dependency(String id, String version) {
        this.id = id;
        this.minVersion = version;
        this.maxVersion = version;
    }

    /**
     * Constructs a new Dependency, from a formatted ID and version.<br>
     * The formatted ID needs to be like so: {@code id:version} or {@code id:minVersion:maxVersion}
     * @param formatted ID and version as a single formatted string.
     */
    public Dependency(String formatted) {
        String[] parts = formatted.split(":");
        this.id = parts[0];
        this.minVersion = parts[1];
        this.maxVersion = parts.length == 2 ? parts[1] : parts[2];
    }
}
