package net.minheur.potoflux.ui.dialogs;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import net.minheur.potoflux.settings.OptionalFeature;
import net.minheur.potoflux.ui.UiUtils;

/**
 * Dialog displayed to add a new optional feature
 */
public class AddFeatureDialog extends Dialog<Pair<String, OptionalFeature>> {

    /**
     * Grid containing all components
     */
    private final GridPane grid;

    /**
     * Combo to choose the type of feature we want to add
     */
    private ComboBox<OptionalFeature.Type> typeCombo;
    /**
     * Field to enter the key (id) of the  field
     */
    private TextField keyField;

    /**
     * Value input field used if {@link OptionalFeature.Type#STRING} is selected in {@linkplain #typeCombo}
     */
    private TextField stringField;
    /**
     * Value input box used if {@link OptionalFeature.Type#BOOL} is selected in {@linkplain #typeCombo}
     */
    private ComboBox<Boolean> boolField;
    /**
     * Value input spinner used if {@link OptionalFeature.Type#INT} is selected in {@linkplain #typeCombo}
     */
    private Spinner<Integer> intField;

    /**
     * Makes the dialog, sets up the layout, adds the buttons and components
     */
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

        setupResult();

    }

    /**
     * Makes the result converter
     */
    private void setupResult() {
        setResultConverter(buttonType -> {

            if (buttonType == UiUtils.confirmButton.get()) {

                String key = keyField.getText().trim();
                if (key.isEmpty()) return null;

                OptionalFeature.Type type = typeCombo.getSelectionModel().getSelectedItem();

                String stringValue = stringField.getText();
                boolean boolValue = boolField.getValue();
                int intValue = intField.getValue();

                OptionalFeature feature;

                switch (type) {
                    case STRING -> feature = new OptionalFeature(stringValue);
                    case INT -> feature = new OptionalFeature(intValue);
                    case BOOL -> feature = new OptionalFeature(boolValue);
                    default -> throw new IllegalStateException("Unexpected value: " + type);
                }

                return new Pair<>(key, feature);

            } else return null;

        });
    }

    /**
     * Adds the buttons to the dialog's button bar
     */
    private void setupButtons() {
        getDialogPane().getButtonTypes().addAll(
                UiUtils.cancelButton.get(),
                UiUtils.confirmButton.get()
        );
    }

    /**
     * Remakes the grid, depending on what type is selected
     * @param type selected in {@link #typeCombo}
     */
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

    /**
     * Adds the {@link #stringField} to the {@linkplain #grid}
     */
    private void addStringField() {
        grid.add(stringField, 1, 2);
    }
    /**
     * Adds the {@link #boolField} to the {@linkplain #grid}
     */
    private void addBoolField() {
        grid.add(boolField, 1, 2);
    }
    /**
     * Adds the {@link #intField} to the {@linkplain #grid}
     */
    private void addIntField() {
        grid.add(intField, 1, 2);
    }

    /**
     * Creates the {@link #stringField}
     */
    private void initStringField() {
        stringField = new TextField();
        stringField.setPromptText("Enter value..."); // todo
    }
    /**
     * Creates the {@link #boolField}
     */
    private void initBoolField() {
        boolField = new ComboBox<>(FXCollections.observableArrayList(true, false));
        boolField.getSelectionModel().select(false);
    }
    /**
     * Creates the {@link #intField}
     */
    private void initIntField() {
        intField = new Spinner<>(
                Integer.MIN_VALUE,
                Integer.MAX_VALUE,
                0
        );
    }

    /**
     * Makes and adds the {@link #keyField} to the {@linkplain #grid}
     */
    private void addKeyField() {
        keyField = new TextField();
        keyField.setPromptText("Enter key..."); // todo
        keyField.setPrefWidth(250);

        grid.add(keyField, 0, 2);
    }

    /**
     * Adds a separator to the {@linkplain #grid}
     */
    private void addSep() {
        Separator separator = new Separator();
        separator.setMaxWidth(Double.MAX_VALUE);

        grid.add(separator, 0, 1);
        GridPane.setColumnSpan(separator, GridPane.REMAINING);
    }

    /**
     * Adds the {@link #typeCombo} to the {@linkplain #grid}, respecting the selected value
     * @param actual selected into the combo, used to keep the actual selected value
     */
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
