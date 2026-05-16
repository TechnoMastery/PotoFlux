package net.minheur.potoflux.ui.dialogs;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import net.minheur.potoflux.login.Account;
import net.minheur.potoflux.translations.Translations;
import net.minheur.potoflux.ui.UiUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Contains the list of all users, each with a button allowing the user to see their details.
 */
public class AllUsersDialog extends Dialog<Void> {

    private final List<Account> accounts;

    private final BorderPane root;
    private VBox listPanel;

    public AllUsersDialog(Frame parent, List<Account> accounts) {
        this.accounts = accounts;
        setTitle("All accounts"); // todo

        root = new BorderPane();

        setupPanel();
        fillAccounts();

        getDialogPane().setContent(root);
        getDialogPane().getButtonTypes().add(UiUtils.closeButton.get());
        getDialogPane().setPrefSize(400, 500);

    }

    private void fillAccounts() {
        for (Account account : accounts)
            listPanel.getChildren().add(mkRow(account));
    }

    private @NotNull HBox mkRow(Account account) {

        HBox row = new HBox(10);
        row.setPadding(new Insets(10));

        row.setBorder(new Border(
                new BorderStroke(
                        Color.LIGHTGRAY,
                        BorderStrokeStyle.SOLID,
                        new CornerRadii(5),
                        BorderWidths.DEFAULT
                )
        ));

        Label emailLabel = new Label(account.email);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button detailsButton = new Button(Translations.get("common:details"));
        detailsButton.setOnAction(e -> showDetails(account));

        row.getChildren().addAll(
                emailLabel,
                spacer,
                detailsButton
        );

        return row;
    }

    private void showDetails(Account account) {

        // AccountDetailsDialog dialog = new AccountDetailsDialog(account);
        // dialog.showAndWait();

    }

    private void setupPanel() {
        listPanel = new VBox(10);
        listPanel.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane(listPanel);
        scrollPane.setFitToWidth(true);

        root.setCenter(scrollPane);
    }
}
