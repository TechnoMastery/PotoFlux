package net.minheur.potoflux.screen.tabs.all;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.screen.tabs.BaseVTab;
import net.minheur.potoflux.utils.LogAmountManager;

/**
 * Debug tab class.
 */
public class DebugTab extends BaseVTab<BorderPane> {
    @Override
    protected void instantiate() {
        PANEL = new BorderPane();
        vContent = new VBox();
        PANEL.setCenter(vContent);
        vContent.getChildren().add(new Label("DEBUG tab"));
    }

    @Override
    protected void setPanel() {
        Button b = new Button("run welcome");
        b.setOnAction((event) -> LogAmountManager.appTour());
        vContent.getChildren().add(b);
    }

    @Override
    public String getName() {
        return "Debug";
    }

    @Override
    protected boolean doPreset() {
        return false;
    }
}