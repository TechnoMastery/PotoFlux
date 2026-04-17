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
     * All logs in {@link #MOD_LOADER} category, under mod update
     */
    MOD_UPDATE(MOD_LOADER, "modUpdate"),
    /**
     * All logs related to your account
     */
    ACCOUNT("account"),
    /**
     * All logs related to posting requests, with your account (this englobes connection and admin actions)
     */
    CONNEXION_POST(ACCOUNT, "requests"),
    /**
     * All logs related to your account's IDs
     */
    ACCOUNT_IDS(ACCOUNT, "IDs"),
    /**
     * All logs related to your account's token
     */
    TOKEN(ACCOUNT, "token"),
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
    private final String[] more;

    /**
     * Registers a log category
     * @param code the code of the category
     */
    LogCategories(String code, String... more) {
        this.code = code;
        this.more = more;
    }

    LogCategories(ILogCategory parent, String... more) {
        this.code = parent.code();
        this.more = more;
    }

    /**
     * Getter for the code
     * @return the category's code
     */
    @Override
    public String code() {
        return code;
    }

    @Override
    public String[] more() {
        return more;
    }
}
