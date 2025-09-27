package net.minheur.potoflux.human.gamete;

public class Gamete {
    private final GameteGender gender;

    public Gamete(GameteGender gender) {
        this.gender = gender;
    }

    public GameteGender getGender() {
        return gender;
    }
}
