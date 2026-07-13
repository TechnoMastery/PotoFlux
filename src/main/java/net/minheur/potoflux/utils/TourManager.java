package net.minheur.potoflux.utils;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import net.minheur.potoflux.PotoFlux;

import java.util.List;

public class TourManager {

    private final StackPane root;
    private final TourOverlay overlay;

    private List<TourStep> steps;
    private int index;

    public TourManager() {
        root = PotoFlux.app.getStack();
        this.overlay = new TourOverlay(this);
    }

    public void start(List<TourStep> steps) {
        this.steps = steps;
        this.index = 0;

        root.getChildren().add(overlay);
        showCurrent();
    }

    private void showCurrent() {
        overlay.show(steps.get(index));
    }
    public void next() {
        index++;
        if (index >= steps.size()) {
            stop();
            return;
        }
        showCurrent();
    }

    private void stop() {
        root.getChildren().remove(overlay);
    }

    public class TourOverlay extends Pane {

        private final Rectangle bg;

        private final VBox popup;

        private final Button next;
        private final Button skip;

        private final Label title;
        private final Label desc;

        public TourOverlay(TourManager manager) {
            bg = new Rectangle();
            bg.widthProperty().bind(root.widthProperty());
            bg.heightProperty().bind(root.heightProperty());

            next = new Button("Next");
            skip = new Button("Skip");

            next.setOnAction(e -> manager.next());
            skip.setOnAction(e -> manager.stop());

            HBox buttons = new HBox(next, skip);

            title = new Label();
            desc = new Label();

            popup = new VBox(title, desc, buttons);
        }

        public void show(TourStep step) {
            getChildren().clear();

            Bounds b = step.target()
                    .localToScene(step.target().getBoundsInLocal());

            Rectangle hole = new Rectangle(
                    b.getMinX() -8,
                    b.getMinY() -8,
                    b.getWidth() +16,
                    b.getHeight() +16
            );

            Shape mask = Shape.subtract(bg, hole);
            mask.setFill(Color.rgb(0, 0, 0, 0.60));

            popup.setLayoutX(b.getMaxX() +25);
            popup.setLayoutY(b.getMinY());

            Line arrow = new Line(
                    popup.getLayoutX(),
                    popup.getLayoutY(),
                    b.getCenterX(),
                    b.getCenterY()
            );

            title.setText(step.title);
            desc.setText(step.desc);

            getChildren().add(mask);
            getChildren().add(arrow);
            getChildren().add(popup);

        }
    }

    public record TourStep(Node target, String title, String desc) {}

}
