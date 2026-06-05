package net.minheur.potoflux.login.response;

import com.google.gson.annotations.SerializedName;

public class IsAccountCreationEnabledResponse {
    /**
     * Return weather account self-creation is enabled.
     */
    @SerializedName("is_enabled")
    public boolean isEnabled;
}
