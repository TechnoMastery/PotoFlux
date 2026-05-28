package net.minheur.potoflux.utils.close;

public enum ExitCode {
    SUCCESS(0),
    UNCAUGHT_EXCEPTION(1),
    BOOTSTRAP_FAILED(2),
    REGISTRATION_FAILED(-1);

    private final int code;

    ExitCode(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }
}
