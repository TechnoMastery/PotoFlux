package net.minheur.potoflux.login.response;

import com.google.gson.JsonObject;

/**
 * Notification list response class.
 */
public class NotificationListResponse extends BaseResponse {
    /**
     * The notifications array field.
     */
    public JsonObject[] notifications;
}
