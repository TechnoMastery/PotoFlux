package net.minheur.potoflux.login.notifications;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minheur.potoflux.Functions;
import net.minheur.potoflux.login.notifications.reg.INotificationType;
import net.minheur.potoflux.login.notifications.reg.NotifTypes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Notification {

    private final long id;
    private final JsonObject messageObj;
    private final String timestamp;

    private final transient INotificationType type;

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
        String title = type.buildTitle(messageObj);
        return title == null ? "No title !" : title;
    }
    public String buildMessage() {
        String message = type.buildMessage(messageObj);
        return message == null ? "No message !" : message;
    }
    public String buildDetail() {
        String detail = type.buildDetails(messageObj);
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
    public INotificationType getType() {
        return type;
    }
}
