package net.minheur.potoflux.datagen;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Generators {
    private static final Path out = Paths.get("generated");

    public static void generate() {
        LangGen.generate(out.resolve("lang"));
    }
}
