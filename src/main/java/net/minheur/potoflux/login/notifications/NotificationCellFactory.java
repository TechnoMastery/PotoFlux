package net.minheur.potoflux.login.notifications;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.*;
import net.minheur.potoflux.ui.UiUtils;

public class NotificationCellFactory extends ListCell<Notification> {

    private final BorderPane root;
    private final GridPane grid;

    private final Label title;
    private final Label message;
    private final Label date;

    private final Button closeBtn;
    private final Button expandBtn;

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

        Region typeBar = new Region();
        typeBar.setPrefWidth(5);
        typeBar.setStyle("-fx-background-color: red;");

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

        setGraphic(root);
    }

    private void showPopup(Notification n) {
        UiUtils.showAlert(
                n.getType().getAlertType(),
                n.buildDetail(),
                n.buildTitle(),
                n.buildMessage() + " - " + n.getFormattedDate(),
                null, null
        );
    }
}
