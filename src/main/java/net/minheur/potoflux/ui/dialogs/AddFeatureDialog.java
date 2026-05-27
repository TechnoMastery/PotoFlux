package net.minheur.potoflux.ui.dialogs;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import net.minheur.potoflux.settings.OptionalFeature;

public class AddFeatureDialog extends Dialog<Pair<String, OptionalFeature>> {

    private final GridPane grid;

    private ComboBox<OptionalFeature.Type> typeCombo;

    public AddFeatureDialog() {

        this.grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));

        addChoseType();
        addSep(1);

        // todo: add choosers

    }

    private void addSep(int row) {
        Separator separator = new Separator();
        separator.setMaxWidth(Double.MAX_VALUE);

        grid.add(separator, 0, row);
        GridPane.setColumnSpan(separator, GridPane.REMAINING);
    }

    private void addChoseType() {
        typeCombo = new ComboBox<>(FXCollections.observableArrayList(
                OptionalFeature.Type.values()
        ));
        typeCombo.setPrefWidth(250);

        grid.add(new Label("Feature type: "), 0, 0); // todo
        grid.add(typeCombo, 1, 0);
    }
}
