package net.minheur.potoflux.life.chromosomes;

import net.minheur.potoflux.life.chromosomes.genes.Gene;
import net.minheur.potoflux.life.chromosomes.genes.GeneType;

import java.util.HashMap;
import java.util.Map;

public class Chromosome {
    private final int id;
    private final Map<GeneType, Gene> genes = new HashMap<>();

    public Chromosome(int id) {
        this.id = id;
    }

    public boolean isGenePresent(GeneType pType) {
        return genes.containsKey(pType);
    }

    public Gene getGene(GeneType pType) {
        return genes.get(pType);
    }

    public void setGene(Gene pGene) {
        GeneType pType = pGene.getType();
        if (isGenePresent(pType)) throw new IllegalArgumentException("Gene " + pType + " is already defined !");
        genes.put(pType, pGene);
    }

    public int getId() {
        return id;
    }
}
