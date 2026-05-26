package net.minheur.potoflux.ui.dialogs;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import net.minheur.potoflux.settings.OptionalFeature;
import net.minheur.potoflux.settings.OptionalFeaturesManager;
import net.minheur.potoflux.ui.UiUtils;

import java.util.Map;

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

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        row.getChildren().addAll(
                keyField, valueNode,
                spacer
        );

        return row;

    }

    private void setupPanel() {
        listPanel = new VBox(10);
        listPanel.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane(listPanel);
        scrollPane.setFitToWidth(true);

        root.setCenter(scrollPane);
    }
}
