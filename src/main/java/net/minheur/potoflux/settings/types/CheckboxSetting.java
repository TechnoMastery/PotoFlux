package net.minheur.potoflux.settings.types;

import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import org.jetbrains.annotations.NotNull;

/**
 * A setting that uses a checkbox, featuring a {@code true} / {@code false} switch
 */
public class CheckboxSetting implements ISettingType<Boolean> {

    /**
     * Label that displays a {@code !} if the data got changed
     */
    private final Label isModifiedLabel;
    /**
     * Actual node displayed to choose the value
     */
    private final CheckBox node;
    /**
     * Default value of the node, used if it was never set
     */
    private final boolean defaultValue;

    /**
     * Constructor using a name and a default value to create the setting
     *
     * @param name         of the setting
     * @param defaultValue of the setting
     */
    public CheckboxSetting(String name, boolean defaultValue) {

        this.node = new CheckBox(name);
        this.defaultValue = defaultValue;
        this.isModifiedLabel = new Label();

        node.setSelected(defaultValue);

    }

    /**
     * Getter for the actual selected value, as an {@link ObservableValue}
     *
     * @return the selected value property
     */
    @Override
    public ObservableValue<Boolean> valueProperty() {
        return node.selectedProperty();
    }

    /**
     * Helper to select a given value into the {@linkplain #node}
     *
     * @param value to select. Should be the same type of {@link #prefType()}
     */
    @Override
    public void selectValue(@NotNull Object value) {

        if (!prefType().getValueClass().isInstance(value))
            throw new IllegalArgumentException(
                    "Invalid type for " + prefType()
            );

        node.setSelected((boolean) value);
    }

    /**
     * Getter for the default value
     *
     * @return {@link #defaultValue}
     */
    @Override
    public @NotNull Boolean getDefaultValue() {
        return defaultValue;
    }

    /**
     * Getter for the selected value<br>
     * Is directly the value, not an observable like {@linkplain #valueProperty()}
     *
     * @return the selected value
     */
    @Override
    public Boolean getSelectedValue() {
        return node.isSelected();
    }

    /**
     * Getter for if the value got changed
     *
     * @return {@link #isModifiedLabel}
     */
    @Override
    public Label getIsModifiedLabel() {
        return isModifiedLabel;
    }

    /**
     * Getter for the checkbox
     *
     * @return {@link #node}
     */
    @Override
    public Node getExecutionNode() {
        return node;
    }

    /**
     * Getter for the settings pref type
     *
     * @return {@link PreferencesTypes#BOOLEAN}
     */
    @Override
    public PreferencesTypes prefType() {
        return PreferencesTypes.BOOLEAN;
    }

}
