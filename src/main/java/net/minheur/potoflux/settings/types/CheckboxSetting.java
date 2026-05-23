package net.minheur.potoflux.settings.types;

import javafx.scene.Node;
import javafx.scene.control.CheckBox;

public class CheckboxSetting implements ISettingType {

    private final CheckBox node;

    public CheckboxSetting(String name) {
        this.node = new CheckBox(name);
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
