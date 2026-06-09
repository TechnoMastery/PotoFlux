package net.minheur.potoflux.login.notifications;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public record Notification(
        long id,
        JsonObject message,
        @SerializedName("created_at")
        String timestamp
) {}
