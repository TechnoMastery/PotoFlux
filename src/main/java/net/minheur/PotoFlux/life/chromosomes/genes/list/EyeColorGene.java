package net.minheur.PotoFlux.life.chromosomes.genes.list;

import net.minheur.PotoFlux.life.chromosomes.genes.AllGenes;
import net.minheur.PotoFlux.life.chromosomes.alleles.EyesColorAlleles;
import net.minheur.PotoFlux.life.chromosomes.genes.Gene;
import net.minheur.PotoFlux.life.chromosomes.genes.GeneType;

public class EyeColorGene extends Gene {
    private final EyesColorAlleles allele;

    public EyeColorGene(EyesColorAlleles allele) {
        this.allele = allele;
    }

    @Override
    public Enum<?> getAllele() {
        return allele;
    }

    @Override
    public GeneType getType() {
        return AllGenes.EYE_COLOR_GENE;
    }
}
