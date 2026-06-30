package net.minheur.potoflux.login.response;

import com.google.gson.annotations.SerializedName;

/**
 * Rm user response class.
 */
public class RmUserResponse extends BaseResponse {
    /**
     * The target's rank.
     */
    @SerializedName("target_rank")
    public int targetRank = 0;
}
