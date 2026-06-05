package net.minheur.potoflux.screen.menu;

import javafx.scene.control.Menu;
import net.minheur.potoflux.registry.IRegistryType;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;

/**
 * Item for a menu
 * @param id of the menu
 * @param content to add to the menu bar
 */
public record PotoMenuItem(ResourceLocation id, Menu content) implements IRegistryType {
}
