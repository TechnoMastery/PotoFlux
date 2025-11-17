package net.minheur.potoflux.modGen.data;

/**
 * @param ordering BEFORE / AFTER / NONE
 * @param side     CLIENT / SERVER / BOTH
 */
public record ModDependency(String modId, boolean mandatory, String versionRange, String ordering, String side) {
    @Override
    public String toString() {
        return modId + " (" + versionRange + ") " +
                (mandatory ? "[MANDATORY]" : "[OPTIONAL]") +
                " - " + ordering + " - " + side;
    }
}
