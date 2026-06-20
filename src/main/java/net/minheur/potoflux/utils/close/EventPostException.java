package net.minheur.potoflux.utils.close;

/**
 * Used to report an exception during event posting stage
 */
public class EventPostException extends RuntimeException {
    public EventPostException(String message) {
        super(message);
    }

    public EventPostException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventPostException(Throwable cause) {
        super(cause);
    }

    public EventPostException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public EventPostException() {
        super();
    }
}
