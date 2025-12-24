package net.minheur.potoflux.utils.logger;

public enum LogCategories implements ILogCategory {
    TERMINAL("terminal"),
    TRANSLATIONS("translations"),
    MOD_LOADER("modLoader"),
    CATALOG("modCatalog"),
    SCREEN("screen");

    private final String code;

    LogCategories(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return code;
    }
}
