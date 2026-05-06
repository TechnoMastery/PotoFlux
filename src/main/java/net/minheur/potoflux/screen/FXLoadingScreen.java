package net.minheur.potoflux.screen;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FXLoadingScreen {

    private Stage stage;
    private Label label;
    private Label stageLabel;

    public void setup() {

        stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Loading Potoflux...");

        label = new Label("Potoflux is loading...");
        stageLabel = new Label();

        VBox root = new VBox(10);
        root.setAlignment(Pos.BOTTOM_RIGHT);
        root.setStyle("-fx-padding: 20;");

        root.getChildren().addAll(label, stageLabel);

        Scene scene = new Scene(root, 800, 350);
        scene.getStylesheets().add(
                getClass().getResource("/styles/loading-screen/loading-screen.css").toExternalForm()
        );
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    public void show() {
        stage.show();
    }
    public void updateStage(String text) {
        stageLabel.setText(text);
    }
    public void close() {
        stage.close();
    }
}
