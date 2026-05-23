package net.minheur.potoflux.settings.types;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ComboSetting<T> implements ISettingType {

    private final HBox content;
    private final Label name;
    private final ComboBox<T> node;

    public ComboSetting(String name, ObservableList<T> items) {

        this.content = new HBox(10);
        this.name = new Label(name);
        this.node = new ComboBox<>(items);

        content.getChildren().addAll(this.name, node);

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
