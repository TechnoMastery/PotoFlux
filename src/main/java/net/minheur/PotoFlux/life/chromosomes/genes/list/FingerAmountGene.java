package net.minheur.PotoFlux.life.chromosomes.genes.list;

import net.minheur.PotoFlux.life.chromosomes.genes.AllGenes;
import net.minheur.PotoFlux.life.chromosomes.genes.Gene;
import net.minheur.PotoFlux.life.chromosomes.genes.IAlleleList;

public class FingerAmountGene extends Gene {
    public FingerAmountGene(Enum<? extends IAlleleList> allele) {
        super(AllGenes.FINGER_AMOUNT_GENE, allele);
    }

    public enum FingerAmountAlleles implements IAlleleList {
        FIVE("GLI+"),
        SIZE("GLI-");

        private final String alleleId;

        FingerAmountAlleles(String alleleId) {
            this.alleleId = alleleId;
        }

        @Override
        public String getAlleleId() {
            return alleleId;
        }
    }
}
