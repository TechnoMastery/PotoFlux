package net.minheur.potoflux.human.chromosomes;

import net.minheur.potoflux.human.chromosomes.genes.Gene;
import net.minheur.potoflux.human.chromosomes.genes.GeneType;

import java.util.HashMap;
import java.util.Map;

public class Chromosome {
    private final Map<GeneType, Gene> genes = new HashMap<>();

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
}
