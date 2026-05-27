package net.minheur.potoflux.ui.dialogs;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import net.minheur.potoflux.settings.OptionalFeature;
import net.minheur.potoflux.ui.UiUtils;

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

        initStringField();
        initBoolField();
        initIntField();

        fillGrid(OptionalFeature.Type.STRING);

        getDialogPane().setContent(grid);

        setupButtons();
        ((Button) getDialogPane().lookupButton(UiUtils.confirmButton.get()))
                .setDefaultButton(true);
        ((Button) getDialogPane().lookupButton(UiUtils.cancelButton.get()))
                .setCancelButton(true);

    }

    private void setupButtons() {
        getDialogPane().getButtonTypes().addAll(
                UiUtils.cancelButton.get(),
                UiUtils.confirmButton.get()
        );
    }

    private void fillGrid(OptionalFeature.Type type) {

        grid.getChildren().clear();

        addChoseType(type);
        addSep();

        addKeyField();
        switch (type) {
            case STRING -> addStringField();
            case INT -> addIntField();
            case BOOL -> addBoolField();
        }

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

    private void addSep() {
        Separator separator = new Separator();
        separator.setMaxWidth(Double.MAX_VALUE);

        grid.add(separator, 0, 1);
        GridPane.setColumnSpan(separator, GridPane.REMAINING);
    }

    private void addChoseType(OptionalFeature.Type actual) {
        typeCombo = new ComboBox<>(FXCollections.observableArrayList(
                OptionalFeature.Type.values()
        ));
        typeCombo.getSelectionModel().select(actual);
        typeCombo.setPrefWidth(250);

        typeCombo.setOnAction(e -> fillGrid(typeCombo.getSelectionModel().getSelectedItem()));

        grid.add(new Label("Feature type: "), 0, 0); // todo
        grid.add(typeCombo, 1, 0);
    }
}
