package net.minheur.potoflux.screen.tabs.all;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import net.minheur.potoflux.screen.tabs.BaseVTab;

/**
 * Debug tab class.
 */
public class DebugTab extends BaseVTab<BorderPane> {
    @Override
    protected void instantiate() {
        PANEL = new BorderPane();
        PANEL.getStyleClass().add("debug-tab");
        vContent = new VBox();
        vContent.getStyleClass().add("debug-content");
        PANEL.setCenter(vContent);
        Label label = new Label("DEBUG tab");
        label.getStyleClass().add("debug-label");
        vContent.getChildren().add(label);
    }

    @Override
    protected void setPanel() {
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