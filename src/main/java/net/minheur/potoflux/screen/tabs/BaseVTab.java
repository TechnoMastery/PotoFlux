package net.minheur.potoflux.screen.tabs;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public abstract class BaseVTab<T extends Pane> extends BaseTab<T> {

    protected VBox vContent;

    @Override
    void runPreset() {
        boxPreset();
        PANEL.getChildren().add(vContent);
        vContent.getChildren().add(mkTitle());
    }

    protected void boxPreset() {
        vContent = new VBox();
        vContent.setSpacing(20);
        vContent.setPadding(new Insets(30, 0, 0, 0));
        vContent.setAlignment(Pos.TOP_CENTER);

        vContent.setPrefWidth(Double.MAX_VALUE);
        vContent.setPrefHeight(Double.MAX_VALUE);

        VBox.setVgrow(vContent, Priority.ALWAYS);
    }

}
