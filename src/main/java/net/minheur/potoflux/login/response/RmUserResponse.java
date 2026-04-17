package net.minheur.potoflux.login.response;

import com.google.gson.annotations.SerializedName;

/**
 * Response when you delete a user.<br>
 * If you fail because of your rank, it will give you the target user's rank.
 */
public class RmUserResponse extends BaseResponse {
    @SerializedName("target_rank")
    public int targetRank;
}
