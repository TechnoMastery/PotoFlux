package net.minheur.potoflux.human;

import net.minheur.potoflux.human.chromosomes.Karyotype;

public class Human {
    protected final int chromosomePairAmount = 23;
    protected final Karyotype karyotype = new Karyotype(this);

    public Karyotype getKaryotype() {
        return karyotype;
    }

}
