package net.minheur.potoflux.life;

import net.minheur.potoflux.life.chromosomes.Chromosome;
import net.minheur.potoflux.life.chromosomes.Karyotype;

import java.util.HashMap;
import java.util.Map;

public abstract class LivingEntity {
    protected final Map<Integer, Chromosome> chromosomes = new HashMap<>();
    protected final Karyotype karyotype = new Karyotype(getChromosomePairAmount(), this);

    protected final int chromosomePairAmount;
    protected final int chromosomeAmount;

    protected LivingEntity(int chromosomePairAmount) {
        this.chromosomePairAmount = chromosomePairAmount;
        this.chromosomeAmount = chromosomePairAmount * 2;
    }

    public int getChromosomesAmount() {
        return chromosomeAmount;
    }

    public int getChromosomePairAmount() {
        return chromosomePairAmount;
    }

    public Chromosome getChromosome(int pChromosomeId) {
        if (chromosomes.get(pChromosomeId) == null) throw new IllegalStateException("This chromosome need to be set !");
        if (pChromosomeId > getChromosomesAmount()) throw new IllegalArgumentException("This chromosome doesn't exist !");
        return chromosomes.get(pChromosomeId);
    }

    public Karyotype getKaryotype() {
        return karyotype;
    }

}
