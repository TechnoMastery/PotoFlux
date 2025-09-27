package net.minheur.potoflux.human.chromosomes;

import net.minheur.potoflux.human.Human;

import java.util.HashMap;
import java.util.Map;

public class Karyotype {
    private final int chromosomePairAmount = 23;
    private final Human human;
    private final Map<Integer, ChromosomePair> chromosomes = new HashMap<>();

    public Karyotype(Human pEntity) {
        this.human = pEntity;

        for (int i = 1; i <= chromosomePairAmount; i++) chromosomes.put(i, new ChromosomePair(i));
    }

    public Human getHuman() {
        return human;
    }
    public ChromosomePair getPair(int id) {
        if (id > chromosomePairAmount) throw new IllegalArgumentException("Id bigger than chromosome amount !");
        if (!chromosomes.containsKey(id)) throw new IllegalStateException("This chromosome must exist but doesn't !");
        return chromosomes.get(id);
    }
}
