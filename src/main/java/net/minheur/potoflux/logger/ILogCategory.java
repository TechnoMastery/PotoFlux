package net.minheur.potoflux.logger;

/**
 * Class for creating log categories.<br>
 * You should implement this in an {@link Enum}
 */
public interface ILogCategory {
    /**
     * Getter to override to get the code.
     * @return the category's code
     */
    String code();
}
