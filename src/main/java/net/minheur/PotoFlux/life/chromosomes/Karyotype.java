package net.minheur.PotoFlux.life.chromosomes;

import net.minheur.PotoFlux.life.LivingEntity;

public class Karyotype {
    private final int chromosomeAmount;
    private final int chromosomePairAmount;
    private final LivingEntity entity;

    public Karyotype(int chromosomePairAmount, LivingEntity pEntity) {
        this.chromosomePairAmount = chromosomePairAmount;
        this.chromosomeAmount = this.chromosomePairAmount*2;
        this.entity = pEntity;
    }

    public int getChromosomeAmount() {
        return chromosomeAmount;
    }
    public int getChromosomePairAmount() {
        return chromosomePairAmount;
    }
    public LivingEntity getEntity() {
        return entity;
    }
}
