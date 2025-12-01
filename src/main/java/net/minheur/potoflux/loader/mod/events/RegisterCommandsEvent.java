package net.minheur.potoflux.loader.mod.events;

import net.minheur.potoflux.terminal.commands.CommandRegistry;

public class RegisterCommandsEvent {
    public final CommandRegistry reg = new CommandRegistry();
}
