package net.minheur.potoflux.loader.mod;

/**
 * Mod container class.
 */
public class ModContainer {
    /**
     * The mod field.
     */
    public Mod mod;
    /**
     * The class field.
     */
    public Class<?> clazz;
    /**
     * The mod loading state.
     */
    public ModState state = ModState.NOT_PROCESSED;

    /**
     * Constructs a new ModContainer.
     */
    public ModContainer(Mod mod, Class<?> clazz) {
        this.mod = mod;
        this.clazz = clazz;
    }
}
