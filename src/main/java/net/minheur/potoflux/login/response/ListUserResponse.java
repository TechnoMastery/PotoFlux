package net.minheur.potoflux.login.response;

/**
 * A response with the list of all UUIDs required to ask for an {@link InfoResponse}.
 */
public class ListUserResponse extends BaseResponse {

    /**
     * List of all user uuid
     */
    public String[] list;

}
