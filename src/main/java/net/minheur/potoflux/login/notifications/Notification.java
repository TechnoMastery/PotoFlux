package net.minheur.potoflux.login.notifications;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import net.minheur.potoflux.Functions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Notification {

    private final long id;
    @SerializedName("message")
    private final JsonObject messageObj;
    @SerializedName("created_at")
    private final String timestamp;

    private transient NotifTypes type;

    public Notification(long id, JsonObject messageObj, String timestamp) {
        this.id = id;
        this.messageObj = messageObj;
        this.timestamp = timestamp;
    }

    public String buildTitle() {
        String title = switch (getType()) {
            case BASIC -> messageObj.get("title").getAsString();
        };

        return title == null ? "No title !" : title;
    }
    public String buildMessage() {
        String message = switch (getType()) {
            case BASIC -> messageObj.get("msg").getAsString();
        };

        return message == null ? "No message !" : message;
    }
    public String buildDetail() {
        String detail = switch (getType()) {
            case BASIC -> messageObj.get("details").getAsString();
        };

        return detail == null ? "No title !" : detail;
    }

    public String getFormattedDate() {
        LocalDateTime dateTime = Functions.parseSqlDate(timestamp);
        return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
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
        if (type == null) {
            JsonElement e = messageObj.get("type");
            if (e == null) type = NotifTypes.BASIC;
            else if (e.getAsString() == null) this.type = NotifTypes.BASIC;
            else this.type = NotifTypes.getFromCode(e.getAsString());
        }

        return type;
    }
}
