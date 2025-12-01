package net.minheur.potoflux.datagen;

import java.nio.file.Path;

public class LangGen {
    public static void generate(Path output) {
        LangBuilder b = new LangBuilder();

        b.add("tabs.home").en("Home").fr("Maison");

        b.save(output);
    }
}
