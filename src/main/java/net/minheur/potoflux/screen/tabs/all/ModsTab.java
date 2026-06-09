package net.minheur.potoflux.screen.tabs.all;

import javafx.geometry.Pos;
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
import java.util.Objects;

public class ModsTab extends BaseVTab<VBox> {

    private VBox entriesBox;
    private ScrollPane scrollPane;

    @Override
    protected void instantiate() {
        PANEL = new VBox();
        PANEL.setFillWidth(true);
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
        vContent = new VBox(20);
        vContent.setAlignment(Pos.TOP_CENTER);

        Label title = mkTitle();

        entriesBox = new VBox(10);
        entriesBox.getStylesheets().add(
                Objects.requireNonNull(
                        getClass().getResource("/styles/tabs/mods/listEntry.css")
                ).toExternalForm()
        );
        entriesBox.getStyleClass().add("modEntries");

        scrollPane = new ScrollPane(entriesBox);
        scrollPane.setFitToWidth(true);

        vContent.getChildren().addAll(title, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        PANEL.getChildren().add(vContent);

        List<ModContainer> allMods = PotoFluxLoadingContext.getListedMods();
        if (!allMods.isEmpty()) for (ModContainer m : allMods)
            addMod(m);
        else {
            Label fallback = new Label("No mods listed !");
            entriesBox.setAlignment(Pos.CENTER);
            entriesBox.getChildren().add(fallback);
        }
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
        state.getStyleClass().add("state");
        state.getStyleClass().add(getStateStyle(modContainer.state));

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
            case FAILED -> "Failed"; // todo
            case INCOMPATIBLE -> "Incompatible"; // todo
            case MISSING_DEPENDENCIES -> "Missing dependencies"; // todo
            case DEPENDENCY_WRONG_VERSION -> "Wrong dependency version"; // todo
            case CIRCULAR -> "Circular dependency"; // todo
            default -> throw new IllegalStateException(
                    "Unexpected state in UI: " + state
            );
        };
    }
    private String getStateStyle(ModState state) {
        return switch (state) {
            case LOADED -> "loaded";
            case MISSING_DEPENDENCIES,
                 DEPENDENCY_WRONG_VERSION -> "depFailed";
            case FAILED,
                 INCOMPATIBLE,
                 CIRCULAR -> "failed";
            default -> throw new IllegalStateException(
                    "Unexpected state in UI: " + state
            );
        };
    }

    @Override
    protected boolean doPreset() {
        return false;
    }
}
