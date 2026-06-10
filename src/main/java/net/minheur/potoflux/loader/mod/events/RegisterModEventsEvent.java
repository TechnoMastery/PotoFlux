package net.minheur.potoflux.loader.mod.events;

import net.minheur.potoflux.loader.mod.post.ModEventsRegistry;

public class RegisterModEventsEvent implements IEvent {
    public final ModEventsRegistry reg = new ModEventsRegistry();

    @Override
    public void close() {
        reg.close();
    }
}
