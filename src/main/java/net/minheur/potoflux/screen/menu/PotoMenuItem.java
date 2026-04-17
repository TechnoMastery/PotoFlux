package net.minheur.potoflux.screen.menu;

import net.minheur.potoflux.registry.IRegistryType;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;

import javax.swing.*;

public record PotoMenuItem(ResourceLocation id, JComponent content) implements IRegistryType {
}
