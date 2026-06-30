package net.minheur.potoflux.login.response;

/**
 * Base response class.
 */
public class BaseResponse {
    /**
     * The success field.
     */
    public boolean success = false;
    /**
     * The error field (in case of failure, {@link #success} is {@code false}
     */
    public String error;
}
