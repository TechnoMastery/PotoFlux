package net.minheur.potoflux.login.notifications;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class Notification {

    private final long id;
    @SerializedName("message")
    private final JsonObject messageObj;
    @SerializedName("created_at")
    private final String timestamp;

    private final transient NotifTypes type;

    public Notification(long id, JsonObject messageObj, String timestamp) {
        this.id = id;
        this.messageObj = messageObj;
        this.timestamp = timestamp;

        String type = messageObj.get("type").getAsString();
        if (type == null) this.type = NotifTypes.BASIC;
        else this.type = NotifTypes.getFromCode(type);
    }

    public String buildTitle() {
        String title = switch (type) {
            case BASIC -> messageObj.get("title").getAsString();
        };

        return title == null ? "No title !" : title;
    }
    public String buildMessage() {
        String message = switch (type) {
            case BASIC -> messageObj.get("msg").getAsString();
        };

        return message == null ? "No message !" : message;
    }
    public String buildDetail() {
        String detail = switch (type) {
            case BASIC -> messageObj.get("details").getAsString();
        };

        return detail == null ? "No title !" : detail;
    }

    public long getId() {
        return id;
    }
    public JsonObject getMessageObj() {
        return messageObj;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public NotifTypes getType() {
        return type;
    }
}
