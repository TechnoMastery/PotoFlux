package net.minheur.potoflux.login.notifications;

import javafx.scene.control.Alert;

public enum NotifTypes {
    BASIC("base", Alert.AlertType.NONE);

    private final String sqlCode;
    private final Alert.AlertType alertType;

    NotifTypes(String sqlCode, Alert.AlertType alertType) {
        this.sqlCode = sqlCode;
        this.alertType = alertType;
    }

    public String getSqlCode() {
        return sqlCode;
    }
    public Alert.AlertType getAlertType() {
        return alertType;
    }

    public static NotifTypes getFromCode(String sqlCode) {
        for (NotifTypes t : NotifTypes.values())
            if (t.sqlCode.equals(sqlCode)) return t;
        return BASIC;
    }
}
