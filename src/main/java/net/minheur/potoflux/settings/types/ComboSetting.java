package net.minheur.potoflux.settings.types;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ComboSetting<T extends IComboSetting> implements ISettingType<T> {

    private final HBox content;
    private final Label isModifiedLabel;
    private final Label name;
    private final ComboBox<T> node;
    private final T defaultValue;

    public ComboSetting(String name, ObservableList<T> items, @NotNull T defaultValue) {

        this.content = new HBox(10);
        this.name = new Label(name);
        this.node = new ComboBox<>(items);
        this.defaultValue = defaultValue;

        isModifiedLabel = new Label();
        this.node.getSelectionModel().select(defaultValue);
        content.getChildren().addAll(this.name, node);

    }

    @Override
    public Label getIsModifiedLabel() {
        return isModifiedLabel;
    }

    @Override
    public void selectValue(@NotNull Object value) {

        if (!prefType().getValueClass().isInstance(value))
            throw new IllegalArgumentException(
                    "Invalid type for " + prefType()
            );

        String key = (String) value;
        T tValue = null;

        for (T t : node.getItems())
            if (t.returnValue().equals(key)) {
                tValue = t;
                break;
            }

        if (tValue == null)
            throw new IllegalArgumentException(
                    "Value " + key + " isn't contained in the list !"
            );

        node.getSelectionModel().select(tValue);

    }

    @Override
    public ObservableValue<T> valueProperty() {
        return node.getSelectionModel().selectedItemProperty();
    }

    @Override
    public String getSelectedValue() {
        return node.getSelectionModel().getSelectedItem().returnValue();
    }

    @Override
    public @NotNull T getDefaultValue() {
        return defaultValue;
    }

    @Override
    public Node getExecutionNode() {
        return content;
    }

    @Override
    public PreferencesTypes prefType() {
        return PreferencesTypes.STRING;
    }
}
