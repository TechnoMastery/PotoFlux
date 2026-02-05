package net.minheur.potoflux.loader.mod.events;

import net.minheur.potoflux.terminal.CommandRegistry;

/**
 * Event to register your commands.
 */
public class RegisterCommandsEvent {
    /**
     * Registry containing all commands.
     */
    public final CommandRegistry reg = new CommandRegistry();
}
