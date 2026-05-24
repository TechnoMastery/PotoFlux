package net.minheur.potoflux.settings.types;

import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CheckboxSetting implements ISettingType<Boolean> {

    private final Label isModifiedLabel;
    private final CheckBox node;
    private final boolean defaultValue;

    public CheckboxSetting(String name, boolean defaultValue) {

        this.node = new CheckBox(name);
        this.defaultValue = defaultValue;
        this.isModifiedLabel = new Label();

        node.setSelected(defaultValue);

        isModifiedLabel.textProperty().bind(
                Bindings.createStringBinding(() ->
                                Objects.equals(node.selectedProperty().get(), defaultValue) ? "" : "!",
                        node.selectedProperty()
                )
        );

    }

    @Override
    public void selectValue(@NotNull Object value) {

        if (!prefType().getValueClass().isInstance(value))
            throw new IllegalArgumentException(
                    "Invalid type for " + prefType()
            );

        node.setSelected((boolean) value);
    }

    @Override
    public @NotNull Boolean getDefaultValue() {
        return defaultValue;
    }

    @Override
    public Boolean getSelectedValue() {
        return node.isSelected();
    }

    @Override
    public Label getIsModifiedLabel() {
        return isModifiedLabel;
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
