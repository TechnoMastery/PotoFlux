package net.minheur.potoflux.loader.mod.events;

import net.minheur.potoflux.login.notifications.reg.NotificationTypesRegistry;

public class RegisterNotifTypesEvent implements IEvent {
    public final NotificationTypesRegistry reg = new NotificationTypesRegistry();

    @Override
    public void close() {
        reg.close();
    }
}
