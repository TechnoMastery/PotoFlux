package net.minheur.potoflux.login.notifications.reg;

import com.google.gson.JsonObject;
import javafx.scene.control.Alert;

/**
 * Notification type interface.
 */
public interface INotificationType {
    /**
     * Gets the notification type's SQL code.
     * @return the notification's SQL code
     */
    String getSqlCode();

    /**
     * Gets the type of the alert to be displayed on details alert
     * @param obj notification object
     * @return the {@link Alert.AlertType}
     */
    Alert.AlertType detailsAlertType(JsonObject obj);

    /**
     * Builds the notif's title.
     * @param obj notification object
     * @return the built notif's title
     */
    String buildTitle(JsonObject obj);

    /**
     * Builds the message of the notif
     * @param obj notification object
     * @return the built message
     */
    String buildMessage(JsonObject obj);

    /**
     * Builds the details of the notif
     * @param obj notification object
     * @return the built details for the notif
     */
    String buildDetails(JsonObject obj);

    /**
     * Class for the color of the notif's sidebar
     * @param obj notification object
     * @return notif's bar color class
     */
    String typeBarColorClass(JsonObject obj);
}
