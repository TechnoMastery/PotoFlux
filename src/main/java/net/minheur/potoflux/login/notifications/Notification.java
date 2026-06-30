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

/**
 * Notification class.
 */
public class Notification {

    /**
     * The id of the notification.
     */
    private final long id;
    /**
     * The message obj the notification.
     */
    private final JsonObject messageObj;
    /**
     * The timestamp the notification.
     */
    private final String timestamp;

    /**
     * The type the notification.
     */
    private final transient INotificationType type;

    /**
     * Constructs a new Notification.
     * @param id ID of the notification
     * @param messageObj message as JSON of the notification
     * @param timestamp time were the notification got created
     */
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

    /**
     * Builds the title.
     * @return the built title
     */
    public String buildTitle() {
        String title = type.buildTitle(messageObj);
        return title == null ? "No title !" : title;
    }

    /**
     * Builds the message.
     * @return the built message
     */
    public String buildMessage() {
        String message = type.buildMessage(messageObj);
        return message == null ? "No message !" : message;
    }

    /**
     * Builds the detail.
     * @return the built details
     */
    public String buildDetail() {
        String detail = type.buildDetails(messageObj);
        return detail == null ? "No details !" : detail;
    }

    /**
     * Gets the detail's {@link Alert.AlertType}.
     * @return the detail's {@link Alert.AlertType}
     */
    public Alert.AlertType detailsAlertType() {
        Alert.AlertType type = this.type.detailsAlertType(messageObj);
        return type == null ? Alert.AlertType.INFORMATION : type;
    }

    /**
     * Gets the formatted date.
     * @return the formatted date
     */
    public String getFormattedDate() {
        LocalDateTime dateTime = Functions.parseSqlDate(timestamp);
        return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    /**
     * Gets the sidebar color's CSS class
     * @return the sidebar color class
     */
    public String getTypeColorClass() {
        String colorClass = type.typeBarColorClass(messageObj);
        return colorClass == null ? "red" : colorClass;
    }

    /**
     * Gets the id.
     * @return {@link #id}
     */
    public long getId() {
        return id;
    }

    /**
     * Gets the message obj.
     * @return {@link #messageObj}
     */
    public JsonObject getMessageObj() {
        return messageObj;
    }

    /**
     * Gets the timestamp.
     * @return {@link #timestamp}
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the type.
     * @return {@link #type}
     */
    public INotificationType getType() {
        return type;
    }
}
