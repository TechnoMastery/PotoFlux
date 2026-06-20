package net.minheur.potoflux.login.notifications.reg;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.scene.control.Alert;
import net.minheur.potoflux.loader.mod.events.RegisterNotifTypesEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

import static net.minheur.potoflux.PotoFlux.fromModId;

public enum NotifTypes implements INotificationType {
    BASIC("base", obj -> Alert.AlertType.INFORMATION, obj -> {
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
    }, obj -> "green"),
    FULL_MANUAL("manual", obj -> {
        JsonElement e = obj.get("alertType");
        if (e == null) return Alert.AlertType.INFORMATION;
        return switch (e.getAsString()) {
            case "info" -> Alert.AlertType.INFORMATION;
            case "warning" -> Alert.AlertType.WARNING;
            case "confirm" -> Alert.AlertType.CONFIRMATION;
            case "error" -> Alert.AlertType.ERROR;
            default -> Alert.AlertType.NONE;
        };
    }, obj -> {
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
    }, obj -> {
        JsonElement e = obj.get("barColorClass");
        if (e == null) return "red";
        else return e.getAsString();
    });

    private final String sqlCode;
    private final Function<JsonObject, Alert.AlertType> alertType;

    private final Function<JsonObject, String> title;
    private final Function<JsonObject, String> message;
    private final Function<JsonObject, String> details;

    private final Function<JsonObject, String> typeBarColorClass;

    NotifTypes(String sqlCode, Function<JsonObject, Alert.AlertType> alertType, Function<JsonObject, String> title, Function<JsonObject, String> message, Function<JsonObject, String> details, Function<JsonObject, String> typeBarColorClass) {
        this.sqlCode = sqlCode;
        this.alertType = alertType;

        this.title = title;
        this.message = message;
        this.details = details;

        this.typeBarColorClass = typeBarColorClass;
    }

    @Override
    public String getSqlCode() {
        return sqlCode;
    }
    @Override
    public Alert.AlertType detailsAlertType(JsonObject obj) {
        return alertType.apply(obj);
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

    @Override
    public String typeBarColorClass(JsonObject obj) {
        return typeBarColorClass.apply(obj);
    }

    public static void register(@NotNull RegisterNotifTypesEvent event) {
        event.reg.add(new NotificationType(
                fromModId("notifTypes"),
                NotifTypes.class
        ));
    }
}
