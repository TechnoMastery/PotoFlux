package net.minheur.potoflux.utils.close;

/**
 * Used to report an exception during event posting stage
 */
public class EventPostException extends RuntimeException {
    /**
     * Constructs a new EventPostException.
     * @param message exception message
     */
    public EventPostException(String message) {
        super(message);
    }

    /**
     * Constructs a new EventPostException.
     * @param message exception message
     * @param cause exception cause
     */
    public EventPostException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new EventPostException.
     * @param cause exception cause
     */
    public EventPostException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new EventPostException.
     * @param message exception message
     * @param cause exception cause
     * @param writableStackTrace weather exception has writable stack trace
     */
    public EventPostException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * Constructs a new EventPostException.
     */
    public EventPostException() {
        super();
    }
}
