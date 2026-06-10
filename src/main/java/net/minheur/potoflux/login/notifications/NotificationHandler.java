package net.minheur.potoflux.login.notifications;

import com.google.gson.JsonObject;
import net.minheur.potoflux.login.InvalidTokenException;
import net.minheur.potoflux.login.RequestPoster;
import net.minheur.potoflux.login.TokenHandler;
import net.minheur.potoflux.login.response.NotificationListResponse;
import net.minheur.potoflux.translations.Translations;
import net.minheur.potoflux.utils.Json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static net.minheur.potoflux.ui.UiUtils.showErrorPane;

public final class NotificationHandler {

    private static final List<Notification> notifications = new ArrayList<>();

    public static void load() {
        if (!TokenHandler.has()) {
            notifications.clear();
            return;
        }

        String content;
        try {
            content = RequestPoster.getNotifications(TokenHandler.get());
        } catch (InvalidTokenException e) {
            e.printStackTrace();
            showErrorPane(Translations.get("potoflux:tabs.account.error.tokenMalformed"));
            TokenHandler.clear();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            showErrorPane(Translations.get("potoflux:tabs.account.failed"));
            return;
        }

        NotificationListResponse response = Json.GSON.fromJson(content, NotificationListResponse.class);

        if (!response.success) {
            showErrorPane(
                    response.error == null ? content :
                    switch (response.error) {
                        case "no_permission" -> Translations.get("potoflux:tabs.account.error.noPerm");
                        case "email_used" -> Translations.get("potoflux:tabs.account.createAccount.emailUsed");
                        default -> response.error;
                    }
            );
            return;
        }

        List<Notification> notifs = new ArrayList<>();

        for (JsonObject obj : response.notifications) {
            if (!obj.has("id")
                    || !obj.has("message")
                    || !obj.has("created_at")) {
                continue;
            }
            try {
                Notification notif = new Notification(
                        obj.get("id").getAsLong(),
                        obj.get("message").getAsJsonObject(),
                        obj.get("created_at").getAsString()
                );
                notifs.add(notif);
            } catch (IllegalStateException | NumberFormatException ignored) {}
        }

        notifications.clear();
        notifications.addAll(notifs);

    }

    public static List<Notification> getNotifications() {
        return List.copyOf(notifications);
    }

}
