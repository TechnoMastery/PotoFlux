package net.minheur.potoflux.terminal;

import net.minheur.potoflux.registry.AbstractRegistry;

/**
 * Reg containing all commands of the app.<br>
 * Commands should be added to this
 */
public class CommandRegistry extends AbstractRegistry<Command> {

    /**
     * Checks if a key is already used by another command
     *
     * @param pKey command key to check if used
     * @return if the key is already used
     */
    public boolean containsKey(String pKey) {
        for (Command command : REGISTRY.values()) if (pKey.equals(command.key())) return true;
        return false;
    }

    /**
     * Gets a command object from his key
     *
     * @param pKey command key to get the command from
     * @return the command associated to the key, or null if the command doesn't exist
     */
    public Command getCommandWithKey(String pKey) {
        for (Command command : REGISTRY.values()) if (pKey.equals(command.key())) return command;
        return null;
    }
}
