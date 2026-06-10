package net.minheur.potoflux.screen.tabs;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Base tab, but also including a preset {@link #vContent} and {@link #boxPreset()}
 * @param <T> type of {@link Pane} added to the tab
 */
public abstract class BaseVTab<T extends Parent> extends BaseTab<T> {

    /**
     * Preset {@link VBox} to add to the panel
     */
    protected VBox vContent;

    /**
     * Preset with the title, content and box preset
     */
    @Override
    void runPreset() {
        if (!(PANEL instanceof Pane)) return;

        boxPreset();
        ((Pane) PANEL).getChildren().add(vContent);
        vContent.getChildren().add(mkTitle());
    }

    /**
     * Preset layout for the {@link #vContent}
     */
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
