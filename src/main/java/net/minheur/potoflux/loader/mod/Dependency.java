package net.minheur.potoflux.loader.mod;

public class Dependency {

    public final String id;
    public final String version;

    public Dependency(String id, String version) {
        this.id = id;
        this.version = version;
    }

    public Dependency(String formated) {
        String[] parts = formated.split(":");
        this.id = parts[0];
        this.version = parts[1];
    }

}
