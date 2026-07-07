package net.minheur.potoflux.ui.dialogs;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import net.minheur.potoflux.logger.PtfLogger;
import net.minheur.potoflux.settings.OptionalFeature;
import net.minheur.potoflux.settings.OptionalFeaturesManager;
import net.minheur.potoflux.ui.UiUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static net.minheur.potoflux.settings.OptionalFeaturesManager.saveToFile;

/**
 * Dialog to list, see and manage optional features added to the app
 */
public class OptionalFeaturesDialog extends Dialog<Void> {

    /**
     * Main pane of the dialog
     */
    private final BorderPane root;
    /**
     * Sub panel of {@linkplain #root} that actually contains the added features
     */
    private VBox listPanel;

    /**
     * Creates the dialog, and adds all components to it
     */
    public OptionalFeaturesDialog() {
        setTitle("Optional features"); // todo

        root = new BorderPane();
        setupPanel();

        fillFeatures();
        mkAddFeatureButton();

        getDialogPane().setContent(root);
        getDialogPane().getButtonTypes().add(UiUtils.closeButton.get());
        getDialogPane().setPrefSize(650, 500);

        ((Button) getDialogPane().lookupButton(UiUtils.closeButton.get()))
                .setDefaultButton(true);
    }

    /**
     * Creates and adds the button to make a new features
     */
    private void mkAddFeatureButton() {
        ButtonType buttonType = new ButtonType(
                "Add a feature", // todo
                ButtonBar.ButtonData.OTHER
        );
        getDialogPane().getButtonTypes().add(buttonType);

        Button button = (Button) getDialogPane().lookupButton(buttonType);

        button.setOnAction(e -> {

            AddFeatureDialog dialog = new AddFeatureDialog();
            Optional<Pair<String, OptionalFeature>> returnValue = dialog.showAndWait();

            if (returnValue.isEmpty()) return;

            String key = returnValue.get().getKey();
            OptionalFeature value = returnValue.get().getValue();

            Map<String, OptionalFeature> features = new HashMap<>(OptionalFeaturesManager.getFeatureMap());
            if (features.containsKey(key)) {
                boolean confirmed = UiUtils.showConfirmationDialog("Key '" + key + "' already exists.\nDo you want to override ?"); // todo
                if (!confirmed) return;
                features.replace(key, value);
            } else features.put(key, value);

            saveAndHandle(features);

        });
    }

    /**
     * Fills in all features from the {@linkplain OptionalFeaturesManager} into the {@linkplain #listPanel}
     */
    private void fillFeatures() {
        if (OptionalFeaturesManager.getFeatureMap().isEmpty()) {
            listPanel.getChildren().add(new Label("No features !")); // todo
            return;
        }

        for (Map.Entry<String, OptionalFeature> entry : OptionalFeaturesManager.getFeatureMap().entrySet())
            listPanel.getChildren().add(mkRow(entry.getKey(), entry.getValue()));
    }

    /**
     * Helper to add a single feature to the {@linkplain #listPanel}
     *
     * @param featureKey used as key in the property file
     * @param value      gets the type and the value of the feature
     * @return the built line for the feature
     */
    private @NotNull HBox mkRow(String featureKey, @NotNull OptionalFeature value) {

        // setup row
        HBox row = new HBox(10);
        row.setPadding(new Insets(10));
        row.setBorder(new Border(
                new BorderStroke(
                        Color.LIGHTGRAY,
                        BorderStrokeStyle.SOLID,
                        new CornerRadii(5),
                        BorderWidths.DEFAULT
                )
        ));

        // data : key & value fields
        TextField keyField = new TextField(featureKey);

        Node valueNode = switch (value.getType()) {
            case STRING -> new TextField((String) value.get());
            case INT -> {
                Spinner<Integer> valueSpinner = new Spinner<>(
                        Integer.MAX_VALUE,
                        Integer.MIN_VALUE,
                        (Integer) value.get()
                );
                valueSpinner.setEditable(true);
                yield valueSpinner;
            }
            case BOOL -> {
                ComboBox<Boolean> valueBool = new ComboBox<>();
                valueBool.getItems().addAll(true, false);
                valueBool.setValue((Boolean) value.get());
                yield valueBool;
            }
        };

        // make spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // mk buttons
        Button modifyButton = new Button("Save changes"); // todo
        modifyButton.setOnAction(e -> saveData(
                featureKey, value,
                keyField, valueNode
        ));

        Button rmButton = new Button("Remove feature"); // todo
        rmButton.setOnAction(e -> rmFeature(featureKey));

        // add to row
        row.getChildren().addAll(
                keyField, valueNode,
                spacer,
                modifyButton, rmButton
        );

        return row;

    }

    /**
     * Removes a feature from the property file
     *
     * @param key of the feature to remove
     */
    private void rmFeature(String key) {

        Map<String, OptionalFeature> features = new HashMap<>(OptionalFeaturesManager.getFeatureMap());
        features.remove(key);
        saveAndHandle(features);

    }

    /**
     * Saves modified feature into the map
     *
     * @param key          original key of the feature
     * @param data         original value container of the feature
     * @param newKeyField  used to get new key
     * @param newValueNode used to get new value
     * @throws ClassCastException if the wrong node is used for a specific type
     */
    @SuppressWarnings("unchecked")
    private void saveData(String key, @NotNull OptionalFeature data, @NotNull TextField newKeyField, Node newValueNode) {

        // easy type access + key + value
        OptionalFeature.Type type = data.getType();
        String newKey = newKeyField.getText().trim();
        Object newValue = switch (type) {
            case STRING -> ((TextField) newValueNode).getText();
            case INT -> ((Spinner<Integer>) newValueNode).getValue();
            case BOOL -> ((ComboBox<Boolean>) newValueNode).getValue();
        };

        // get if key / value changed
        boolean keyChanged = !newKey.equals(key);
        boolean valueChanged = !Objects.equals(newValue, data.get());

        if (!(keyChanged || valueChanged)) return; // return if not changed
        if (newKey.isEmpty()) return; // return if empty key

        OptionalFeature newData = switch (type) {
            case STRING -> new OptionalFeature((String) newValue);
            case INT -> new OptionalFeature((Integer) newValue);
            case BOOL -> new OptionalFeature((Boolean) newValue);
        };

        Map<String, OptionalFeature> featureMap = OptionalFeaturesManager.getFeatureMap();
        featureMap.remove(key);
        featureMap.put(
                keyChanged ? newKey : key,
                valueChanged ? newData : data
        );

        saveAndHandle(featureMap);

    }

    /**
     * Asks for writing in a file ({@linkplain OptionalFeaturesManager#saveToFile(Map)}) and handle the exception or success
     *
     * @param features map of features to save. Contains all previous features, but take in count the modifications
     */
    private void saveAndHandle(Map<String, OptionalFeature> features) {
        try {
            saveToFile(features);
        } catch (IOException e) {
            e.printStackTrace();
            PtfLogger.error("Failed to save optionalFeature.properties !");
            UiUtils.showErrorPane("Failed to save property !"); // todo
        } finally {
            OptionalFeaturesManager.load();
            listPanel.getChildren().clear();
            fillFeatures();
        }
    }

    /**
     * Sets up the {@linkplain #listPanel} and {@linkplain #root} layouts
     */
    private void setupPanel() {
        listPanel = new VBox(10);
        listPanel.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane(listPanel);
        scrollPane.setFitToWidth(true);

        root.setCenter(scrollPane);
    }
}
