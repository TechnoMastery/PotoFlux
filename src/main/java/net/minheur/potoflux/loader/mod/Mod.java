package net.minheur.potoflux.loader.mod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Mod {
    String modId();
    String version();
    String[] compatibleVersions() default {"-1"};
    String compatibleVersionUrl() default "NONE";
}
