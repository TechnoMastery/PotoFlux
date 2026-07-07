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
        vContent = new VBox();
        vContent.getStyleClass().add("debug");
        PANEL.setCenter(vContent);
        Label title = new Label("DEBUG tab");
        title.getStyleClass().add("title");
        vContent.getChildren().add(title);
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