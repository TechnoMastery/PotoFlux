package net.minheur.potoflux.ui.dialogs;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import net.minheur.potoflux.settings.OptionalFeature;

public class AddFeatureDialog extends Dialog<Pair<String, OptionalFeature>> {

    private final GridPane grid;

    private ComboBox<OptionalFeature.Type> typeCombo;
    private TextField keyField;

    private TextField stringField;
    private ComboBox<Boolean> boolField;
    private Spinner<Integer> intField;

    public AddFeatureDialog() {

        this.grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));

        addChoseType();
        addSep(1);

        initStringField();
        initBoolField();
        initIntField();

        addKeyField();
        addStringField();

    }

    private void addStringField() {
        grid.add(stringField, 1, 2);
    }
    private void addBoolField() {
        grid.add(boolField, 1, 2);
    }
    private void addIntField() {
        grid.add(intField, 1, 2);
    }

    private void initStringField() {
        stringField = new TextField();
        stringField.setPromptText("Enter value..."); // todo
    }
    private void initBoolField() {
        boolField = new ComboBox<>(FXCollections.observableArrayList(true, false));
        boolField.getSelectionModel().select(false);
    }
    private void initIntField() {
        intField = new Spinner<>(
                Integer.MIN_VALUE,
                Integer.MAX_VALUE,
                0
        );
    }

    private void addKeyField() {
        keyField = new TextField();
        keyField.setPromptText("Enter key..."); // todo
        keyField.setPrefWidth(250);

        grid.add(keyField, 0, 2);
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
        typeCombo.getSelectionModel().select(OptionalFeature.Type.STRING);
        typeCombo.setPrefWidth(250);

        grid.add(new Label("Feature type: "), 0, 0); // todo
        grid.add(typeCombo, 1, 0);
    }
}
