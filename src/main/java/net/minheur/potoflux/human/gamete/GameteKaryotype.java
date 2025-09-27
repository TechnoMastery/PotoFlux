package net.minheur.potoflux.human.gamete;

import net.minheur.potoflux.human.chromosomes.Chromosome;

import java.util.HashMap;
import java.util.Map;

public class GameteKaryotype {
    private final Map<Integer, Chromosome> chromosomes = new HashMap<>();

    public GameteKaryotype() {
        for (int i = 1; i <= 23; i++) chromosomes.put(i, new Chromosome());
    }

    public Chromosome getChromosome(int id) {
        if (id > 23) throw new IllegalArgumentException("Id bigger than chromosome amount !");
        if (!chromosomes.containsKey(id)) throw new IllegalStateException("This chromosome must exist but doesn't !");
        return chromosomes.get(id);
    }

}
