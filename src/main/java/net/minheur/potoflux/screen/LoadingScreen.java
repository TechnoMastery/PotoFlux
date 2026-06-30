package net.minheur.potoflux.screen;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Potoflux's loading screen
 */
public class LoadingScreen {

    /**
     * Stage to display data on
     */
    private Stage stage;
    /**
     * Label telling that potoflux is loading
     */
    private Label label;
    /**
     * Label specifying what is loading
     */
    private Label stageLabel;

    /**
     * Setups the loading screen, makes the stage, labels and layout
     */
    public void setup() {

        stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Loading Potoflux...");

        label = new Label("Potoflux is loading...");
        stageLabel = new Label();

        VBox root = new VBox(10);
        root.setAlignment(Pos.BOTTOM_RIGHT);

        root.getChildren().addAll(label, stageLabel);

        Scene scene = new Scene(root, 800, 350);
        scene.getStylesheets().add(
                getClass().getResource("/styles/loading-screen/loading-screen.css").toExternalForm()
        );
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    /**
     * Displays the {@link #stage}
     */
    public void show() {
        stage.show();
    }

    /**
     * Update what is currently loading
     *
     * @param text to display in {@link #stageLabel}
     */
    public void updateStage(String text) {
        stageLabel.setText(text);
    }

    /**
     * Shuts up the loading screen, mission ended
     */
    public void close() {
        stage.close();
    }
}
