package net.minheur.potoflux.logger;

/**
 * Enum containing all potoflux log categories
 */
public enum LogCategories implements ILogCategory {
    /**
     * All logs talking about the potoflux terminal
     */
    TERMINAL("terminal"),
    /**
     * All logs about the translation system
     */
    TRANSLATIONS("translations"),
    /**
     * All logs related to loading mods
     */
    MOD_LOADER("modLoader"),
    /**
     * All logs about the mod catalog
     */
    CATALOG("modCatalog"),
    /**
     * All logs about the main screen system
     */
    SCREEN("screen"),
    /**
     * All logs about the theme
     */
    THEME("theme");

    /**
     * The actual String to be printed in the log
     */
    private final String code;

    /**
     * Registers a log category
     * @param code the code of the category
     */
    LogCategories(String code) {
        this.code = code;
    }

    /**
     * Getter for the code
     * @return the category's code
     */
    @Override
    public String code() {
        return code;
    }
}
