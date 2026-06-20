package net.minheur.potoflux.utils.ressourcelocation;

/**
 * Thrown when there's an error in a ResourceLocation
 */
public class ResourceLocationException extends RuntimeException {
    public ResourceLocationException(String message) {
        super(message);
    }

    public ResourceLocationException(String message, Throwable cause) {
        super(message, cause);
    }
}
