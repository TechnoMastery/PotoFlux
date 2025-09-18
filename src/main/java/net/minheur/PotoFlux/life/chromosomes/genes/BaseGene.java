package net.minheur.PotoFlux.life.chromosomes.genes;

public abstract class BaseGene {
    private final GeneType GENE_TYPE;
    private final Enum<?> allele;

    protected BaseGene(GeneType geneType, Enum<? extends IAlleleList> allele) {
        GENE_TYPE = geneType;
        this.allele = allele;
    }

    public GeneType getType() {
        return GENE_TYPE;
    }

    public Enum<?> getAllele() {
        return allele;
    }

}
