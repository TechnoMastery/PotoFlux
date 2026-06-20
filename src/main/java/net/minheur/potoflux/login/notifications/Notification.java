package net.minheur.potoflux.login.notifications;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.scene.control.Alert;
import net.minheur.potoflux.Bootstrap;
import net.minheur.potoflux.Functions;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.login.notifications.reg.INotificationType;
import net.minheur.potoflux.login.notifications.reg.NotifTypes;
import net.minheur.potoflux.login.notifications.reg.NotificationType;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

public class Notification {

    private final long id;
    private final JsonObject messageObj;
    private final String timestamp;

    private final transient INotificationType type;

    public Notification(long id, @NotNull JsonObject messageObj, String timestamp) {
        this.id = id;
        this.messageObj = messageObj;
        this.timestamp = timestamp;

        JsonElement e = messageObj.get("type");
        if (e == null) type = NotifTypes.BASIC;
        else if (e.getAsString() == null) this.type = NotifTypes.BASIC;
        else {
            INotificationType tempType = NotifTypes.BASIC;
            List<NotificationType> notifTypes = Bootstrap.notificationTypesEvent.reg.getAll()
                    .stream()
                    .sorted(Comparator.comparing(
                            typeClass -> !typeClass.id().getNamespace().equals(PotoFlux.ID)
                    )).toList();

            for (NotificationType t : notifTypes) {
                boolean found = false;
                if (!t.clazz().isEnum()) continue;
                for (INotificationType notif : t.clazz().getEnumConstants())
                    if (notif.getSqlCode().equals(e.getAsString())) {
                        tempType = notif;
                        found = true;
                        break;
                    }
                if (found) break;
            }
            this.type = tempType;
        }
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

    public Alert.AlertType detailsAlertType() {
        Alert.AlertType type = this.type.detailsAlertType(messageObj);
        return type == null ? Alert.AlertType.INFORMATION : type;
    }

    public String getFormattedDate() {
        LocalDateTime dateTime = Functions.parseSqlDate(timestamp);
        return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public String getTypeColorClass() {
        String colorClass = type.typeBarColorClass(messageObj);
        return colorClass == null ? "red" : colorClass;
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
