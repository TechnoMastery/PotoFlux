package net.minheur.potoflux.life.chromosomes.genes.list;

import net.minheur.potoflux.life.chromosomes.genes.AllGenes;
import net.minheur.potoflux.life.chromosomes.genes.Gene;
import net.minheur.potoflux.life.chromosomes.genes.IAlleleList;

public class EarlobeShapeGene extends Gene {
    public EarlobeShapeGene(Enum<? extends IAlleleList> allele) {
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
