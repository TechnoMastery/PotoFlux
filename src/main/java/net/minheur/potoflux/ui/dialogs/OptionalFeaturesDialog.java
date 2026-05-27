package net.minheur.potoflux.ui.dialogs;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.logger.PtfLogger;
import net.minheur.potoflux.settings.OptionalFeature;
import net.minheur.potoflux.settings.OptionalFeaturesManager;
import net.minheur.potoflux.ui.UiUtils;
import org.jetbrains.annotations.NotNull;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class OptionalFeaturesDialog extends Dialog<Void> {

    private final BorderPane root;
    private VBox listPanel;

    public OptionalFeaturesDialog() {
        setTitle("Optional features"); // todo

        root = new BorderPane();
        setupPanel();

        fillFeatures();

        getDialogPane().setContent(root);
        getDialogPane().getButtonTypes().add(UiUtils.closeButton.get());
        getDialogPane().setPrefSize(400, 500);

        ((Button) getDialogPane().lookupButton(UiUtils.closeButton.get()))
                .setDefaultButton(true);
    }

    private void fillFeatures() {
        for (Map.Entry<String, OptionalFeature> entry : OptionalFeaturesManager.getFeatureMap().entrySet())
            listPanel.getChildren().add(mkRow(entry.getKey(), entry.getValue()));
    }

    private HBox mkRow(String featureKey, OptionalFeature value) {

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

        // add to row
        row.getChildren().addAll(
                keyField, valueNode,
                spacer,
                modifyButton
        );

        return row;

    }

    @SuppressWarnings("unchecked")
    private void saveData(String key, @NotNull OptionalFeature data, @NotNull TextField newKeyField, Node newValueNode) {

        // easy type access + key + value
        OptionalFeature.Type type = data.getType();
        String newKey = newKeyField.getText();
        Object newValue = switch (type) {
            case STRING -> ((TextField) newValueNode).getText();
            case INT -> ((Spinner<Integer>) newValueNode).getValue();
            case BOOL -> ((ComboBox<Boolean>) newValueNode).getValue();
        };

        // get if key / value changed
        boolean keyChanged = !newKey.equals(key);
        boolean valueChanged = !Objects.equals(newValue, data.get());

        if (!(keyChanged || valueChanged)) return; // return if not changed
        if (newKey.trim().isEmpty()) return; // return if empty key

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

    private void saveToFile(@NotNull Map<String, OptionalFeature> featureMap) throws IOException {
        Path featuresPath = PotoFlux.getProgramDir().resolve("optionalFeatures.properties");
        Properties props = new Properties();

        for (Map.Entry<String, OptionalFeature> entry : featureMap.entrySet()) {

            String key = entry.getKey();
            Object value = entry.getValue().get();

            if (value != null)
                props.setProperty(key, String.valueOf(value));

        }

        try (FileOutputStream out = new FileOutputStream(featuresPath.toFile())) {
            props.store(out, "Potoflux's optional features");
        }
    }

    private void setupPanel() {
        listPanel = new VBox(10);
        listPanel.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane(listPanel);
        scrollPane.setFitToWidth(true);

        root.setCenter(scrollPane);
    }
}
