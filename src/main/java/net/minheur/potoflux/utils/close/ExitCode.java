package net.minheur.potoflux.utils.close;

/**
 * Different exit codes that can be used to close the app, making it easy to debug
 */
public enum ExitCode {
    /**
     * Used when the app closed successfully
     */
    SUCCESS(0),
    /**
     * Used when there were a random exception has been thrown in the main thread
     */
    UNCAUGHT_EXCEPTION(1),
    /**
     * Used when boot failed
     */
    BOOTSTRAP_FAILED(2),
    /**
     * Used when a registration failing error got thrown, usually traducing a duped id
     */
    REGISTRATION_FAILED(-1);

    /**
     * Code used to close the app in {@link System#exit}
     */
    private final int code;

    /**
     * Makes an exit code
     *
     * @param code used to exit the app
     */
    ExitCode(int code) {
        this.code = code;
    }

    /**
     * Gets the actual code
     *
     * @return {@link #code}
     */
    public int code() {
        return code;
    }
}
