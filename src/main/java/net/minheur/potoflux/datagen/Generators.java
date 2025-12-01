package net.minheur.potoflux.datagen;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Generators {
    private static final Path out = Paths.get("src/generated/resources");

    public static void generate() {
        LangGen.generate(out.resolve("lang"));
    }

    public static void main(String[] args) {
        generate();
    }
}
