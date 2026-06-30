package net.minheur.potoflux.login.notifications;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import net.minheur.potoflux.ui.UiUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Notification cell factory class.
 */
public class NotificationCellFactory extends ListCell<Notification> {

    /**
     * The root pane.
     */
    private final BorderPane root;
    /**
     * The grid pane.
     */
    private final GridPane grid;

    /**
     * The title label.
     */
    private final Label title;
    /**
     * The message label.
     */
    private final Label message;
    /**
     * The date label.
     */
    private final Label date;

    /**
     * The close button.
     */
    private final Button closeBtn;
    /**
     * The expand notif button.
     */
    private final Button expandBtn;

    /**
     * The type bar.
     */
    private final Region typeBar;

    /**
     * Constructs a new NotificationCellFactory.
     */
    public NotificationCellFactory() {

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.ALWAYS);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.NEVER);
        col2.setHalignment(HPos.RIGHT);

        this.grid = new GridPane();

        grid.getColumnConstraints().addAll(col1, col2);
        grid.setHgap(10);
        grid.setVgap(5);

        this.title = new Label();
        this.date = new Label();
        this.message = new Label();
        title.setFont(Font.font("Consolas", 13));
        message.setWrapText(true);

        this.closeBtn = new Button("✕");
        this.expandBtn = new Button("Expand"); // todo

        closeBtn.setOnAction(e -> getListView().getItems().remove(getItem()));
        expandBtn.setOnAction(e -> showPopup(getItem()));

        grid.add(title, 0, 0);
        grid.add(closeBtn, 1, 0);

        grid.add(message, 0, 1, 2, 1);

        grid.add(expandBtn, 0, 2);
        grid.add(date, 1, 2);

        this.typeBar = new Region();
        typeBar.setPrefWidth(5);

        this.root = new BorderPane();
        root.setCenter(grid);
        root.setRight(typeBar);

        BorderPane.setMargin(grid, new Insets(0, 5, 10, 0));

    }

    @Override
    protected void updateItem(Notification item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setGraphic(null);
            return;
        }

        title.setText(item.buildTitle());
        message.setText(item.buildMessage());
        String formattedDate = item.getFormattedDate();
        date.setText(formattedDate == null ? "" : formattedDate);

        typeBar.getStyleClass().addAll("notifType", item.getTypeColorClass());

        setGraphic(root);
    }

    /**
     * Shows the popup.
     * @param n notif to show the alert of
     */
    private void showPopup(@NotNull Notification n) {
        UiUtils.showAlert(
                n.detailsAlertType(),
                n.buildDetail(),
                n.buildTitle(),
                n.buildMessage() + " - " + n.getFormattedDate(),
                null, null
        );
    }
}
