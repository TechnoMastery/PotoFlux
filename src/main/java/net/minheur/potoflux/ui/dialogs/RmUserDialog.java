package net.minheur.potoflux.ui.dialogs;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import net.minheur.potoflux.Functions;
import net.minheur.potoflux.translations.Translations;
import net.minheur.potoflux.ui.UiUtils;

import javax.swing.*;
import java.awt.*;

/**
 * This dialog asks an admin for a username, to delete his account.
 */
public class RmUserDialog extends Dialog<String> {

    private GridPane grid;

    private TextField rmUserEmail;

    public RmUserDialog() {
        setTitle("Remove a user"); // todo
        initUI();
    }

    private void initUI() {

        setupButtons();
        setupForm();

        addEmail();

        getDialogPane().setContent(grid);

        ((javafx.scene.control.Button) getDialogPane().lookupButton(UiUtils.validateButton.get()))
                .setDefaultButton(true);
        ((Button) getDialogPane().lookupButton(UiUtils.cancelButton.get()))
                .setCancelButton(true);

        setupResult();
    }

    private void setupButtons() {
        getDialogPane().getButtonTypes().addAll(
                UiUtils.cancelButton.get(),
                UiUtils.validateButton.get()
        );
    }

    private void showConfirm() {
        int check = JOptionPane.showConfirmDialog(null,
                Functions.formatMessage(
                        Translations.get("potoflux:tabs.account.rmUser.check"),
                        rmUserEmail.getText()
                ),
                Translations.get("common:confirm"),
                JOptionPane.YES_NO_OPTION
        );
    }

    private void setupResult() {
        setResultConverter(buttonType -> {

            if (buttonType == UiUtils.validateButton.get()) {

                if (false) return null; // todo: confirmation

                return rmUserEmail.getText().trim();

            } else return null;

        });
    }

    private void addEmail() {
        rmUserEmail = new TextField();
        rmUserEmail.setPrefWidth(250);

        grid.add(new Label(Translations.get("common:emailField")), 0, 0);
        grid.add(rmUserEmail, 1, 0);
    }

    private void setupForm() {
        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));
    }

}
