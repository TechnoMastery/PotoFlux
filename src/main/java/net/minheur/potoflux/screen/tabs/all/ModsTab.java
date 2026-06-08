package net.minheur.potoflux.screen.tabs.all;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.*;
import net.minheur.potoflux.loader.PotoFluxLoadingContext;
import net.minheur.potoflux.loader.mod.Dependency;
import net.minheur.potoflux.loader.mod.Mod;
import net.minheur.potoflux.loader.mod.ModContainer;
import net.minheur.potoflux.loader.mod.ModState;
import net.minheur.potoflux.screen.tabs.BaseVTab;

import java.util.List;

public class ModsTab extends BaseVTab<VBox> {

    private VBox entriesBox;
    private ScrollPane scrollPane;

    @Override
    protected void instantiate() {
        PANEL = new VBox();
        entriesBox = new VBox(10);

        scrollPane = new ScrollPane(entriesBox);
        scrollPane.setFitToWidth(true);

        scrollPane.setPrefHeight(Double.MAX_VALUE);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
    }

    @Override
    public String getName() {
        return "Mods"; // todo
    }

    @Override
    protected String getTitle() {
        return "All Mods listed"; // todo
    }

    @Override
    protected void setPanel() {
        vContent.getChildren().add(scrollPane);

        List<ModContainer> allMods = PotoFluxLoadingContext.getListedMods();
        for (ModContainer m : allMods)
            addMod(m);
    }

    private void addMod(ModContainer modContainer) {
        Mod mod = modContainer.mod;

        VBox details = new VBox(5);

        Label depsTitle = new Label("Dependencies"); // todo
        VBox depsBox = new VBox(3);

        for (String formattedDep : mod.dependenciesIds()) {
            Dependency dep = new Dependency(formattedDep);

            Label depLabel = new Label(formatDepLabel(dep));
            depsBox.getChildren().add(depLabel);
        }

        details.getChildren().addAll(depsTitle, depsBox);

        TitledPane pane = new TitledPane();
        pane.setGraphic(buildHeader(modContainer));
        pane.setContent(details);

        pane.setExpanded(false);
        entriesBox.getChildren().add(pane);

    }

    private String formatDepLabel(Dependency dep) {
        if (dep.minVersion.equals(dep.maxVersion))
            return dep.id + " [" + dep.minVersion + "]";

        return dep.id + " [" + dep.minVersion + " - " + dep.maxVersion + "]";
    }

    private Node buildHeader(ModContainer modContainer) {
        HBox root = new HBox(10);

        Label name = new Label(modContainer.mod.modId());
        Label version = new Label(modContainer.mod.version());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label state = new Label(formatState(modContainer.state));
        state.setStyle(getStateStyle(modContainer.state));

        root.getChildren().addAll(
                name, version,
                spacer,
                state
        );

        return root;
    }

    private String formatState(ModState state) {
        return switch (state) {
            case LOADED -> "Loaded"; // todo
            case FAILED -> "Failed";
            case INCOMPATIBLE -> "Incompatible";
            case MISSING_DEPENDENCIES -> "Missing dependencies";
            case DEPENDENCY_WRONG_VERSION -> "Wrong dependency version";
            case CIRCULAR -> "Circular dependency";
            default -> throw new IllegalStateException(
                    "Unexpected state in UI: " + state
            );
        };
    }
    private String getStateStyle(ModState state) {
        return switch (state) {
            case LOADED -> "-fx-text-fill: green;";
            case FAILED,
                 INCOMPATIBLE,
                 MISSING_DEPENDENCIES,
                 DEPENDENCY_WRONG_VERSION,
                 CIRCULAR -> "-fx-text-fill: red;";
            default -> throw new IllegalStateException(
                    "Unexpected state in UI: " + state
            );
        };
    }

}
