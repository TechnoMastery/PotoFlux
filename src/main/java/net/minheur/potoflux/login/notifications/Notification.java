package net.minheur.potoflux.login.notifications;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minheur.potoflux.Functions;
import net.minheur.potoflux.login.notifications.reg.NotifTypes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Notification {

    private final long id;
    private final JsonObject messageObj;
    private final String timestamp;

    private final transient NotifTypes type;

    public Notification(long id, JsonObject messageObj, String timestamp) {
        this.id = id;
        this.messageObj = messageObj;
        this.timestamp = timestamp;

        JsonElement e = messageObj.get("type");
        if (e == null) type = NotifTypes.BASIC;
        else if (e.getAsString() == null) this.type = NotifTypes.BASIC;
        else this.type = NotifTypes.getFromCode(e.getAsString());
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

        return detail == null ? "No details !" : detail;
    }

    public String getFormattedDate() {
        LocalDateTime dateTime = Functions.parseSqlDate(timestamp);
        return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public String getTypeColor() {
        return "red";
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
