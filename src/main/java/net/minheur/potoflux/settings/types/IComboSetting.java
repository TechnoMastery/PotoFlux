package net.minheur.potoflux.settings.types;

/**
 * Helper interface, allowing to keep the {@code toString()} method in displaying the name
 */
public interface IComboSetting {

    /**
     * Gets the value to get stored in the prefs for a given item
     * @return the value to store in the prefs
     */
    String returnValue();

}
