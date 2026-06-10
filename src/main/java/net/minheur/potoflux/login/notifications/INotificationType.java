package net.minheur.potoflux.login.notifications;

import javafx.scene.control.Alert;

public interface INotificationType {
    String getSqlCode();
    Alert.AlertType detailsAlertType();
}
