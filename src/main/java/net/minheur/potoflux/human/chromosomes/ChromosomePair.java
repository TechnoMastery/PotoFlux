package net.minheur.potoflux.human.chromosomes;

public class ChromosomePair {
    private final int pairId;
    private final Chromosome chromosomeMale = new Chromosome();
    private final Chromosome chromosomeFemale = new Chromosome();

    public ChromosomePair(int pairId) {
        this.pairId = pairId;
    }

    public Chromosome getChromosomeMale() {
        return chromosomeMale;
    }

    public Chromosome getChromosomeFemale() {
        return chromosomeFemale;
    }

    public int getID() {
        return pairId;
    }
}
