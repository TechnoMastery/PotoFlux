package net.minheur.potoflux.login.response;

import com.google.gson.annotations.SerializedName;

public class RmUserResponse extends BaseResponse {
    @SerializedName("target_rank")
    public int targetRank;
}
