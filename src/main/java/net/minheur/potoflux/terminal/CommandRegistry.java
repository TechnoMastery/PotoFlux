package net.minheur.potoflux.terminal;

import net.minheur.potoflux.registry.IRegistry;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CommandRegistry implements IRegistry<Command> {
    public static final CommandRegistry INSTANCE = new CommandRegistry();

    private static final Map<ResourceLocation, Command> REGISTRY = new HashMap<>();

    public static Collection<Command> getAll() {
        return REGISTRY.values();
    }

    @Override
    public Command add(Command item) {
        if (REGISTRY.containsKey(item.getId())) throw new IllegalArgumentException("This command is already added: " + item.getId());
        if (containsKey(item.getKey())) throw new IllegalArgumentException("This command key is already used: " + item.getId());
        return REGISTRY.put(item.getId(), item);
    }

    public static boolean containsKey(String pKey) {
        for (Command command : REGISTRY.values()) if (pKey.equals(command.getKey())) return true;
        return false;
    }

    public static Command getCommandWithKey(String pKey) {
        for (Command command : REGISTRY.values()) if (pKey.equals(command.getKey())) return command;
        return null;
    }
}
