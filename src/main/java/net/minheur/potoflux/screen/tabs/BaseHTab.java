package net.minheur.potoflux.screen.tabs;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public abstract class BaseHTab<T extends Pane> extends BaseTab<T> {

    protected HBox hContent;

    @Override
    void runPreset() {
        boxPreset();
        PANEL.getChildren().add(hContent);
        hContent.getChildren().add(mkTitle());
    }

    protected void boxPreset() {
        hContent = new HBox();
        hContent.setSpacing(20);
        hContent.setPadding(new Insets(30, 0, 0, 0));
        hContent.setAlignment(Pos.TOP_CENTER);

        hContent.setPrefWidth(Double.MAX_VALUE);
        hContent.setPrefHeight(Double.MAX_VALUE);

        HBox.setHgrow(hContent, Priority.ALWAYS);
    }
}
