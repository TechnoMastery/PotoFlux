package net.minheur.potoflux.loader.mod;

public class Dependency {

    public final String id;

    public final String minVersion;
    public final String maxVersion;

    public Dependency(String id, String version) {
        this.id = id;
        this.minVersion = version;
        this.maxVersion = version;
    }

    public Dependency(String formated) {
        String[] parts = formated.split(":");
        this.id = parts[0];

        this.minVersion = parts[1];
        if (parts.length == 2)
            this.maxVersion = parts[1];
        else
            this.maxVersion = parts[2];
    }

}
