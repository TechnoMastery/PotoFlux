package net.minheur.potoflux.settings.types;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.jetbrains.annotations.NotNull;

/**
 * A setting using a combo, allowing lots of setting types in one
 * @param <T> type of data stored in the combo.
 */
public class ComboSetting<T extends IComboSetting> implements ISettingType<String> {

    /**
     * Container for the combo
     */
    private final HBox content;
    /**
     * Label that displays a {@code !} if the data got changed
     */
    private final Label isModifiedLabel;
    /**
     * Name label of the setting
     */
    private final Label name;
    /**
     * Actual node displayed to choose the value
     */
    private final ComboBox<T> node;
    /**
     * Default value of the node, used if it was never set
     */
    private final T defaultValue;

    /**
     * Constructor creating the {@linkplain #content} and {@linkplain #node}, as well as the labels.
     * @param name of the setting
     * @param items list of all items to add to the combo. It will be disabled if there are less than 2 items. It needs at least 1 item.
     * @param defaultValue used if there are no saved values. Needs to be contained by the items list
     */
    public ComboSetting(String name, ObservableList<T> items, @NotNull T defaultValue) {

        this.content = new HBox(10);
        this.name = new Label(name);
        this.node = new ComboBox<>(items);
        this.defaultValue = defaultValue;

        if (items.size() < 2) node.setDisable(true);

        isModifiedLabel = new Label();
        this.node.getSelectionModel().select(defaultValue);
        content.getChildren().addAll(this.name, node);

    }

    /**
     * Getter for if the value got changed
     * @return {@link #isModifiedLabel}
     */
    @Override
    public Label getIsModifiedLabel() {
        return isModifiedLabel;
    }

    /**
     * Helper to select a given value into the {@linkplain #node}
     * @param value to select. Should be the same type of {@link #prefType()}
     */
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

    /**
     * Getter for the actual selected value, as an {@link ObservableValue}
     * @return the selected value property
     */
    @Override
    public ObservableValue<T> valueProperty() {
        return node.getSelectionModel().selectedItemProperty();
    }

    /**
     * Getter for the selected value<br>
     * Is the {@linkplain String} value, got though {@linkplain ISettingType}
     * @return the selected value
     */
    @Override
    public String getSelectedValue() {
        return node.getSelectionModel().getSelectedItem().returnValue();
    }

    /**
     * Getter for the default value
     * @return return value of {@link #defaultValue}
     */
    @Override
    public @NotNull String getDefaultValue() {
        return defaultValue.returnValue();
    }

    /**
     * Getter for the combo panel
     * @return {@link #content}
     */
    @Override
    public Node getExecutionNode() {
        return content;
    }

    /**
     * Getter for the settings pref type
     * @return {@link PreferencesTypes#STRING}
     */
    @Override
    public PreferencesTypes prefType() {
        return PreferencesTypes.STRING;
    }
}
