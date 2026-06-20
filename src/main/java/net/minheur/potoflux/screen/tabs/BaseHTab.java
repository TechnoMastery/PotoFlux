package net.minheur.potoflux.screen.tabs;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

/**
 * Base tab, but also including a preset {@link #hContent} and {@link #boxPreset()}
 *
 * @param <T> type of {@link Pane} added to the tab
 */
public abstract class BaseHTab<T extends Parent> extends BaseTab<T> {

    /**
     * Preset {@link HBox} to add to the panel
     */
    protected HBox hContent;

    /**
     * Preset with the title, content and box preset
     */
    @Override
    void runPreset() {
        if (!(PANEL instanceof Pane)) return;

        boxPreset();
        ((Pane) PANEL).getChildren().add(hContent);
        hContent.getChildren().add(mkTitle());
    }

    /**
     * Preset layout for the {@link #hContent}
     */
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
