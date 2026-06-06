package net.minheur.potoflux.login.response;

/**
 * When you log in with your IDs, you receive this with your token.
 */
public class LoginResponse extends BaseResponse {
    /**
     * Once connected, this stores your newly created token.
     */
    public String token;
}
