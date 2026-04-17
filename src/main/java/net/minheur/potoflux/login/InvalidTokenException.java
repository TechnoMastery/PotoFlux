package net.minheur.potoflux.login;

/**
 * This exception is thrown when your token is invalid, which means you maybe have modified it.
 */
public class InvalidTokenException extends Exception {
    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException() {}

    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTokenException(Throwable cause) {
        super(cause);
    }
}
