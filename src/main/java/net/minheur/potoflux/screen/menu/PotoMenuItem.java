package net.minheur.potoflux.screen.menu;

import javafx.scene.control.Menu;
import net.minheur.potoflux.registry.IRegistryType;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;

public record PotoMenuItem(ResourceLocation id, Menu content) implements IRegistryType {
}
