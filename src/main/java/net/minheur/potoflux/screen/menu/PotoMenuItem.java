package net.minheur.potoflux.screen.menu;

import javafx.scene.control.Menu;
import net.minheur.potoflux.registry.IRegistryType;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;

import javax.swing.*;
import java.util.function.Supplier;

public record PotoMenuItem(ResourceLocation id, Supplier<Menu> content) implements IRegistryType {

    public Menu getNode() {
        return content.get();
    }

}
