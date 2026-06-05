package net.minheur.potoflux.login.response;

/**
 * A simple response with a success and error field
 */
public class BaseResponse {

    /**
     * Defines if the action was a success
     */
    public boolean success;
    /**
     * If {@link #success} is {@code false}, this stores the error sent by the database.
     */
    public String error;

}
