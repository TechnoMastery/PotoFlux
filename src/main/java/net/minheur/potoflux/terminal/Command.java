package net.minheur.potoflux.terminal;

import net.minheur.potoflux.registry.IRegistryType;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;

import java.util.List;
import java.util.function.Consumer;

public record Command(ResourceLocation id, String key, Consumer<List<String>> commandOutput, String commandHelp,
                      boolean hidden) implements IRegistryType {
    public Command(ResourceLocation id, String key, Consumer<List<String>> commandOutput, String commandHelp) {

        this(id, key, commandOutput, commandHelp, false);
    }

    public Command(ResourceLocation id, String key, Consumer<List<String>> commandOutput) {

        this(id, key, commandOutput, null, true);
    }
}
