package net.minheur.potoflux.loader.mod.events;

import net.minheur.potoflux.loader.mod.post.ModEventsRegistry;

/**
 * Register mod events event class.
 */
public class RegisterModEventsEvent implements IEvent {
    /**
     * The reg field.
     */
    public final ModEventsRegistry reg = new ModEventsRegistry();

    @Override
    public void close() {
        reg.close();
    }
}
