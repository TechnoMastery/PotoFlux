package net.minheur.PotoFlux.life.chromosomes.genes.list;

import net.minheur.PotoFlux.life.chromosomes.genes.AllGenes;
import net.minheur.PotoFlux.life.chromosomes.genes.BaseGene;
import net.minheur.PotoFlux.life.chromosomes.genes.IAlleleList;

public class EarlobeShapeGene extends BaseGene {
    protected EarlobeShapeGene(Enum<? extends IAlleleList> allele) {
        super(AllGenes.EARLOBE_SHAPE_GENE, allele);
    }

    public enum EarlobeShapeAlleles implements IAlleleList {
        FREE("L"),
        ATTACHED("a");

        private final String alleleId;
        EarlobeShapeAlleles(String alleleId) {
            this.alleleId = alleleId;
        }

        @Override
        public String getAlleleId() {
            return alleleId;
        }
    }
}
