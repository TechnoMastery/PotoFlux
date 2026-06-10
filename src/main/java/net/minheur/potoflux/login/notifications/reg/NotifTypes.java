package net.minheur.potoflux.login.notifications.reg;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.scene.control.Alert;
import net.minheur.potoflux.loader.mod.events.RegisterNotifTypesEvent;

import java.util.function.Function;

import static net.minheur.potoflux.PotoFlux.fromModId;

public enum NotifTypes implements INotificationType {
    BASIC("base", Alert.AlertType.INFORMATION, obj -> {
        JsonElement e = obj.get("title");
        if (e == null) return "No title !";
        else return e.getAsString();
    }, obj -> {
        JsonElement e = obj.get("msg");
        if (e == null) return "No message !";
        else return e.getAsString();
    }, obj -> {
        JsonElement e = obj.get("details");
        if (e == null) return "No details !";
        else return e.getAsString();
    });

    private final String sqlCode;
    private final Alert.AlertType alertType;

    private final Function<JsonObject, String> title;
    private final Function<JsonObject, String> message;
    private final Function<JsonObject, String> details;

    NotifTypes(String sqlCode, Alert.AlertType alertType, Function<JsonObject, String> title, Function<JsonObject, String> message, Function<JsonObject, String> details) {
        this.sqlCode = sqlCode;
        this.alertType = alertType;

        this.title = title;
        this.message = message;
        this.details = details;
    }

    @Override
    public String getSqlCode() {
        return sqlCode;
    }
    @Override
    public Alert.AlertType detailsAlertType() {
        return alertType;
    }

    @Override
    public String buildTitle(JsonObject obj) {
        return title.apply(obj);
    }
    @Override
    public String buildMessage(JsonObject obj) {
        return message.apply(obj);
    }
    @Override
    public String buildDetails(JsonObject obj) {
        return details.apply(obj);
    }

    public static NotifTypes getFromCode(String sqlCode) {
        for (NotifTypes t : NotifTypes.values())
            if (t.sqlCode.equals(sqlCode)) return t;
        return BASIC;
    }
    public static void register(RegisterNotifTypesEvent event) {
        event.reg.add(new NotificationType(
                fromModId("notifTypes"),
                NotifTypes.class
        ));
    }
}
