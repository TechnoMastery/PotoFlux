package net.minheur.potoflux.screen;

import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.loader.PotoFluxLoadingContext;
import net.minheur.potoflux.screen.menu.PotoMenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FXPotoScreen {

    private final Stage stage;
    private final MenuBar menu = new MenuBar();
    private final List<PotoMenuItem> menuItems = new ArrayList<>();

    public FXPotoScreen() {
        stage = new Stage();

        BorderPane root = new BorderPane();

        setupStage();
        // addIcon(); TODO
        // addMenu(); TODO
        // addTabs();

        root.setTop(menu);
        // root.setCenter(tabs);

        Scene scene = new Scene(root, 854, 512);
        stage.setScene(scene);
        stage.show();
    }

    private void setupStage() {
        stage.setTitle("Potoflux");

        stage.setOnCloseRequest(e -> {
            e.consume(); // anti auto-close
            PotoFlux.runProgramClosing(0);
        });

        Properties optionalFeatures = PotoFluxLoadingContext.getOptionalFeatures();
        boolean isResizable = Boolean.parseBoolean(optionalFeatures.getProperty("resizableWindow", "false"));
        stage.setResizable(isResizable);
    }
}
