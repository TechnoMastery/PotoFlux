package net.minheur.potoflux.loader.mod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

/**
 * The class to annotate your main mod class with.<br>
 * This contains all main data for the mod (that doesn't need to create the mod to be accessed).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Mod {
    /**
     * The mod ID of your mod. Should be a unique identifier for your mod.
     * @return your mod's ID
     */
    String modId();
    /**
     * The version of your mod.
     * @return your mod's version
     */
    String version();
    /**
     * The list of all potoflux versions your mod is compatible with.<br>
     * Should take the form of {@code compatibleVersions = {"6.0", "6.1"}}
     * @return potoflux versions compatible with your mod
     */
    String[] compatibleVersions();
}
