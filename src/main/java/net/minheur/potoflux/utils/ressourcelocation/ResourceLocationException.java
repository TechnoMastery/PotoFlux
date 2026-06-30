package net.minheur.potoflux.utils.ressourcelocation;

/**
 * Thrown when there's an error in a ResourceLocation
 */
public class ResourceLocationException extends RuntimeException {
    /**
     * Constructs a new ResourceLocationException.
     */
    public ResourceLocationException(String message) {
        super(message);
    }

    /**
     * Constructs a new ResourceLocationException.
     */
    public ResourceLocationException(String message, Throwable cause) {
        super(message, cause);
    }
}
