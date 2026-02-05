package net.minheur.potoflux.terminal;

import net.minheur.potoflux.registry.IRegistry;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Reg containing all commands of the app.<br>
 * Commands should be added to this
 */
public class CommandRegistry implements IRegistry<Command> {
    /**
     * Actual registry for the commands, by their ID and their command.
     */
    private static final Map<ResourceLocation, Command> REGISTRY = new HashMap<>();

    /**
     * Getter for all commands
     * @return all commands
     */
    public static Collection<Command> getAll() {
        return REGISTRY.values();
    }

    /**
     * Adds a command to the reg
     * @param item command to add to the reg
     * @return the added command
     */
    @Override
    public Command add(Command item) {
        if (REGISTRY.containsKey(item.id())) throw new IllegalArgumentException("This command is already added: " + item.id());
        if (containsKey(item.key())) throw new IllegalArgumentException("This command key is already used: " + item.id());
        return REGISTRY.put(item.id(), item);
    }

    /**
     * Checks if a key is already used by another command
     * @param pKey command key to check if used
     * @return if the key is already used
     */
    public static boolean containsKey(String pKey) {
        for (Command command : REGISTRY.values()) if (pKey.equals(command.key())) return true;
        return false;
    }

    /**
     * Gets a command object from his key
     * @param pKey command key to get the command from
     * @return the command associated to the key, or null if the command doesn't exist
     */
    public static Command getCommandWithKey(String pKey) {
        for (Command command : REGISTRY.values()) if (pKey.equals(command.key())) return command;
        return null;
    }
}
