package net.minheur.potoflux.screen.tabs.all;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import net.minheur.potoflux.Functions;
import net.minheur.potoflux.login.CreateAccountHandler;
import net.minheur.potoflux.login.RequestPoster;
import net.minheur.potoflux.login.perms.Perms;
import net.minheur.potoflux.screen.tabs.BaseVTab;
import net.minheur.potoflux.translations.Translations;

import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;

import static net.minheur.potoflux.ui.UiUtils.showErrorPane;
import static net.minheur.potoflux.login.ConnectionHandler.*;

/**
 * Tab class for account tab
 */
public class AccountTab extends BaseVTab<StackPane> {

    private Label titleLabel;
    private Label emailLabel;

    private VBox permsPanel;
    private ListView<Perms> permsList;
    private ObservableList<Perms> permsModel;
    private Button executePerm;

    private Button authButton;
    private Button createAccountButton;

    @Override
    protected void setPanel() {
        initComponents();
        setupLayout();
        setupActions();
        reload();
    }

    @Override
    protected void instantiate() {
        PANEL = new StackPane();
        vContent = new VBox(10);

        PANEL.getChildren().add(vContent);
    }

    private void initComponents() {
        titleLabel = new Label();
        titleLabel.setFont(Font.font("Consolas", FontWeight.BOLD, 20));

        emailLabel = new Label();
        emailLabel.setFont(Font.font("Consolas", 13));

        permsModel = FXCollections.observableArrayList();
        permsList = new ListView<>(permsModel);

        executePerm = new Button(Translations.get("potoflux:tabs.account.executePermButton"));

        permsPanel = new VBox(5, permsList, executePerm);
        permsPanel.setMaxHeight(150);
        permsPanel.setAlignment(Pos.CENTER);

        authButton = new Button();
        createAccountButton = new Button(Translations.get("potoflux:tabs.account.createAccount.button"));

        vContent.setAlignment(Pos.TOP_CENTER);
    }

    private void setupLayout() {
        vContent.getChildren().addAll(
                titleLabel,
                emailLabel,
                permsPanel,
                authButton,
                createAccountButton
        );
    }

    private void setupActions() {
        authButton.setOnAction(e -> performAuthAction());

        executePerm.setOnAction(e -> {
            if (!isLogged) return;

            Perms selected = permsList.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showErrorPane(Translations.get("potoflux:tabs.account.error.noPermChosen"));
                return;
            }
            if (selected.getPermAction() == null) {

                if (selected.getNoRunFallback() == null)
                    showErrorPane(Translations.get("potoflux:tabs.account.error.noPermRun"));
                else showErrorPane(selected.getNoRunFallback());

                return;
            }

            selected.getPermAction().run();
        });

        createAccountButton.setOnAction(e -> {
            if (isLogged) return;
            CreateAccountHandler.create();
        });
    }

    public void reload() {
        updateTitle();
        updateEmail();
        updatePerms();
        updateButton();
    }

    private void updateTitle() {
        if (isLogged)
            titleLabel.setText(Functions.formatMessage(
                    Translations.get("potoflux:tabs.account.title.connected"),
                    account.firstName, account.lastName
            ));
        else titleLabel.setText(Translations.get("potoflux:tabs.account.title"));
    }

    private void updateEmail() {
        if (isLogged) {
            emailLabel.setVisible(true);
            emailLabel.setText(account.email);
        }
        else emailLabel.setVisible(false);
    }

    private void updatePerms() {
        permsModel.clear();

        if (isLogged) {
            permsPanel.setVisible(account.perms.length > 0);

            for (Perms perm : Perms.values())
                if (Arrays.asList(account.perms).contains(perm))
                    permsModel.add(perm);
        }
        else permsPanel.setVisible(false);
    }

    private void updateButton() {
        authButton.setText(getAuthButtonStatus());
        createAccountButton.setVisible(!isLogged);
        createAccountButton.setDisable(!isAccountCreationEnabled);
    }

    @Override
    protected boolean doPreset() {
        return false;
    }
}
