package net.minheur.potoflux.terminal;

import net.minheur.potoflux.registry.IRegistryType;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

/**
 * A class used to register a command.
 * @param id the resource loc of the command
 * @param key the key to write in the terminal in the terminal
 * @param commandOutput the output to print in the terminal. It takes a list of command args
 * @param commandHelp something to print when the command is wrongly used, or with the {@code help} command.
 * @param hidden if the command is hidden (if true, the {@code commandHelp} should be null).
 */
public record Command(ResourceLocation id, String key, Consumer<List<String>> commandOutput, @Nullable String commandHelp,
                      boolean hidden) implements IRegistryType {
    /**
     * Creates the command.<br>
     * It is a normal command
     * @param id the resource loc of the command
     * @param key the key to write in the terminal in the terminal
     * @param commandOutput the output to print in the terminal. It takes a list of command args
     * @param commandHelp something to print when the command is wrongly used, or with the help command.
     */
    public Command(ResourceLocation id, String key, Consumer<List<String>> commandOutput, @Nonnull String commandHelp) {

        this(id, key, commandOutput, commandHelp, false);
    }

    /**
     * Creates the command.<br>
     * It is a hidden command
     * @param id the resource loc of the command
     * @param key the key to write in the terminal in the terminal
     * @param commandOutput the output to print in the terminal. It takes a list of command args
     */
    public Command(ResourceLocation id, String key, Consumer<List<String>> commandOutput) {

        this(id, key, commandOutput, null, true);
    }
}
