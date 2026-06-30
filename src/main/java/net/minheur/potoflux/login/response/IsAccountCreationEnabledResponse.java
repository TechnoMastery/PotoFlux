package net.minheur.potoflux.login.response;

import com.google.gson.annotations.SerializedName;

/**
 * Response for weather the account creation is enabled
 */
public class IsAccountCreationEnabledResponse {
    /**
     * Weather account creation is enabled
     */
    @SerializedName("is_enabled")
    public boolean isEnabled = false;
}
