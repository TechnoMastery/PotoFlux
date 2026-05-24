package net.minheur.potoflux.settings.types;

import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import org.jetbrains.annotations.NotNull;

public class CheckboxSetting implements ISettingType {

    private final CheckBox node;
    private final boolean defaultValue;

    public CheckboxSetting(String name, boolean defaultValue) {
        this.node = new CheckBox(name);
        this.defaultValue = defaultValue;
    }

    @Override
    public void selectValue(Object value) {

        boolean parsed = Boolean.parseBoolean(value.toString());
        node.setSelected(parsed);

    }

    @Override
    public @NotNull Object getDefaultValue() {
        return defaultValue;
    }

    @Override
    public Node getExecutionNode() {
        return node;
    }

    @Override
    public PreferencesTypes prefType() {
        return PreferencesTypes.BOOLEAN;
    }

}
