package net.minheur.potoflux.login;

/**
 * This exception is thrown when your token is invalid, which means you maybe have modified it.
 */
public class InvalidTokenException extends Exception {
    /**
     * Constructor with a simple message
     *
     * @param message of the exception
     */
    public InvalidTokenException(String message) {
        super(message);
    }

    /**
     * Empty init
     */
    public InvalidTokenException() {
    }

    /**
     * Constructor with a message and a cause
     *
     * @param message of the exception
     * @param cause   of the exception
     */
    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with only a cause
     *
     * @param cause of the exception
     */
    public InvalidTokenException(Throwable cause) {
        super(cause);
    }
}
