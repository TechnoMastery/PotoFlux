package net.minheur.potoflux.settings.types;

import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;

public interface ISettingType<S> {

    Node getExecutionNode();
    PreferencesTypes prefType();

    /**
     * Needs to be the type of the {@link PreferencesTypes#getValueClass()} associated in {@link #prefType()}
     * @return the default value for the setting
     */
    @NotNull S getDefaultValue();
    void selectValue(@NotNull S value);

}
