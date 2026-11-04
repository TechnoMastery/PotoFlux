package net.minheur.potoflux.loader.mod.events;

import net.minheur.potoflux.login.notifications.reg.NotificationTypesRegistry;

/**
 * Register notif types event class.
 */
@Deprecated(since = "8.0.2", forRemoval = true)
public class RegisterNotifTypesEvent implements IEvent {
    /**
     * The reg field.
     */
    public final NotificationTypesRegistry reg = new NotificationTypesRegistry();

    @Override
    public void close() {
        reg.close();
    }
}
