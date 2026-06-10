package net.minheur.potoflux.login.notifications;

import javafx.scene.control.Alert;

public enum NotifTypes implements INotificationType {
    BASIC("base", Alert.AlertType.INFORMATION);

    private final String sqlCode;
    private final Alert.AlertType alertType;

    NotifTypes(String sqlCode, Alert.AlertType alertType) {
        this.sqlCode = sqlCode;
        this.alertType = alertType;
    }

    @Override
    public String getSqlCode() {
        return sqlCode;
    }
    @Override
    public Alert.AlertType detailsAlertType() {
        return alertType;
    }

    public static NotifTypes getFromCode(String sqlCode) {
        for (NotifTypes t : NotifTypes.values())
            if (t.sqlCode.equals(sqlCode)) return t;
        return BASIC;
    }
}
