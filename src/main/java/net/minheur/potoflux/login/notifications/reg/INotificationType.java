package net.minheur.potoflux.login.notifications.reg;

import com.google.gson.JsonObject;
import javafx.scene.control.Alert;

public interface INotificationType {
    String getSqlCode();
    Alert.AlertType detailsAlertType();

    String buildTitle(JsonObject message);
    String buildMessage(JsonObject message);
    String buildDetails(JsonObject message);
}
