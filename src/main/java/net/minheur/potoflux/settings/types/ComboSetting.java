package net.minheur.potoflux.settings.types;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.jetbrains.annotations.NotNull;

public class ComboSetting<T> implements ISettingType<T> {

    private final HBox content;
    private final Label name;
    private final ComboBox<T> node;
    private final T defaultValue;

    public ComboSetting(String name, ObservableList<T> items, @NotNull T defaultValue) {

        this.content = new HBox(10);
        this.name = new Label(name);
        this.node = new ComboBox<>(items);
        this.defaultValue = defaultValue;

        content.getChildren().addAll(this.name, node);

    }

    @Override
    public void selectValue(@NotNull T value) {
        node.getSelectionModel().select(value);
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
