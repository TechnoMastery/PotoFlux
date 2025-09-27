package net.minheur.potoflux.human.chromosomes.genes;

public abstract class Gene {
    private final GeneType GENE_TYPE;
    private final Enum<? extends IAlleleList> allele;

    public Gene(GeneType geneType, Enum<? extends IAlleleList> allele) {
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
