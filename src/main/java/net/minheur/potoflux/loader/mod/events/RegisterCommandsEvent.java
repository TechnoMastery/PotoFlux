package net.minheur.potoflux.loader.mod.events;

import net.minheur.potoflux.terminal.CommandRegistry;

/**
 * Event to register your commands.
 */
public class RegisterCommandsEvent implements IEvent {
    /**
     * Registry containing all commands.
     */
    public final CommandRegistry reg = new CommandRegistry();

    @Override
    public void close() {
        reg.close();
    }
}
