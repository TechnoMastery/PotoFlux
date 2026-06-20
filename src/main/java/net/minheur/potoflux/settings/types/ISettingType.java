package net.minheur.potoflux.settings.types;

import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Label;
import org.jetbrains.annotations.NotNull;

/**
 * Interface used to create setting type
 *
 * @param <S> type of setting data. Needs to be one of {@linkplain PreferencesTypes#getValueClass()}, because only them are supported in the prefs saving.
 * @see CheckboxSetting
 * @see ComboSetting
 */
public interface ISettingType<S> {

    /**
     * Gets the node to pair with {@linkplain #getIsModifiedLabel()} when adding the setting to the list.
     *
     * @return the node that allows to choose the setting's state
     */
    Node getExecutionNode();

    /**
     * Paired with {@linkplain #getExecutionNode()}, it allows the user to know that the value is modified but not yet saved, by displaying a {@code !}.<br>
     * Will also get a restart reminder graphic if the setting needs to do so to be saved.
     *
     * @return the modified state label
     */
    Label getIsModifiedLabel();

    /**
     * Type of preference this is. Used to check that selected value or value to select is the right type
     *
     * @return the {@link PreferencesTypes} of the setting
     */
    PreferencesTypes prefType();

    /**
     * Needs to be the type of the {@link PreferencesTypes#getValueClass()} associated in {@link #prefType()}
     *
     * @return the default value for the setting
     */
    @NotNull S getDefaultValue();

    /**
     * Selects a value from an object.
     *
     * @param value objet containing the value to select
     */
    void selectValue(@NotNull Object value);

    /**
     * Gets the selected value directly as raw
     *
     * @return the raw selected value
     */
    Object getSelectedValue();

    /**
     * Gets the property for the selected value, allowing to check if exists or bind it to FX components
     *
     * @return an {@link ObservableValue} of the selected value
     */
    ObservableValue<?> valueProperty();

}
