package net.minheur.potoflux.terminal;

import net.minheur.potoflux.registry.IRegistryType;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;

import java.util.List;
import java.util.function.Consumer;

public class Command implements IRegistryType {
    private final ResourceLocation id;
    private final String key;
    private final Consumer<List<String>> commandOutput;
    private final String commandHelp;
    private final boolean hidden;

    public Command(ResourceLocation id, String key, Consumer<List<String>> commandOutput, String commandHelp, boolean hidden) {
        this.id = id;
        this.key = key;
        this.commandOutput = commandOutput;
        this.commandHelp = commandHelp;
        this.hidden = hidden;
    }
    public Command(ResourceLocation id, String key, Consumer<List<String>> commandOutput, String commandHelp) {
        this.id = id;
        this.key = key;
        this.commandOutput = commandOutput;
        this.commandHelp = commandHelp;

        this.hidden = false;
    }
    public Command(ResourceLocation id, String key, Consumer<List<String>> commandOutput) {
        this.commandOutput = commandOutput;
        this.key = key;
        this.id = id;

        this.commandHelp = null;
        this.hidden = true;
    }

    public ResourceLocation getId() {
        return id;
    }
    public String getKey() {
        return key;
    }
    public Consumer<List<String>> getCommandOutput() {
        return commandOutput;
    }
    public String getCommandHelp() {
        return commandHelp;
    }
    public boolean isHidden() {
        return hidden;
    }
}
