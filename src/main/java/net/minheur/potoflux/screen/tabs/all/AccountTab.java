package net.minheur.potoflux.screen.tabs.all;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import net.minheur.potoflux.Functions;
import net.minheur.potoflux.login.ConnectionHandler;
import net.minheur.potoflux.login.CreateAccountHandler;
import net.minheur.potoflux.login.perms.Perms;
import net.minheur.potoflux.screen.tabs.BaseVTab;
import net.minheur.potoflux.translations.Translations;

import javax.swing.*;
import java.util.Arrays;

import static net.minheur.potoflux.ui.UiUtils.showErrorPane;
import static net.minheur.potoflux.login.ConnectionHandler.*;

/**
 * Tab class for account tab
 */
public class AccountTab extends BaseVTab<ScrollPane> {

    /**
     * Title of the tab
     */
    private Label titleLabel;
    /**
     * Label for the user's email<br>
     * Hidden if not connected ({@linkplain ConnectionHandler#isLogged} is {@code false})
     */
    private Label emailLabel;

    /**
     * Pane for the perms<br>
     * Contains the {@linkplain #permsList} and the {@linkplain #executePerm}
     */
    private VBox permsPanel;
    /**
     * Actual list for all perms.<br>
     * Uses the {@linkplain #permsModel}
     */
    private ListView<Perms> permsList;
    /**
     * Model for the {@linkplain #permsList}
     */
    private ObservableList<Perms> permsModel;
    /**
     * Executes the perm actually slected on the {@linkplain #permsList}
     */
    private Button executePerm;

    /**
     * Button to connect or disconnect<br>
     * Executes {@link ConnectionHandler#performAuthAction()}
     */
    private Button authButton;
    /**
     * Creates an account.<br>
     * Hidden if connected ({@linkplain ConnectionHandler#isLogged} is {@code true})
     */
    private Button createAccountButton;

    private VBox notificationPane;
    private ListView<String> notificationList;
    private ObservableList<String> notificationModel;

    /**
     * Create and add all components to the panel
     */
    @Override
    protected void setPanel() {
        initComponents();
        setupLayout();
        setupActions();
        reload();
    }

    /**
     * Gets the tab name, displayed in the tab list
     */
    @Override
    public String getName() {
        return Translations.get("potoflux:tabs.account.name");
    }

    /**
     * Instancies all main panels
     */
    @Override
    protected void instantiate() {
        PANEL = new ScrollPane();
        PANEL.setFitToWidth(true);
        PANEL.setFitToHeight(true);

        PANEL.setPannable(true);

        PANEL.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        PANEL.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        vContent = new VBox(10);
        vContent.setPadding(new Insets(30, 20, 30, 20));
        vContent.setMinHeight(Region.USE_PREF_SIZE);

        PANEL.setContent(vContent);
    }

    /**
     * Instancies all components
     */
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

        notificationModel = FXCollections.observableArrayList();
        notificationList = new ListView<>(notificationModel);
        notificationList.setFocusTraversable(false);

        notificationPane = new VBox(5);
        notificationPane.getChildren().addAll(
                new Label("Notifications"), // todo
                notificationList
        );

        notificationPane.setMaxSize(200, 200);
        notificationPane.setAlignment(Pos.CENTER);

        vContent.setAlignment(Pos.TOP_CENTER);
    }

    /**
     * Adds all components to the panels
     */
    private void setupLayout() {
        vContent.getChildren().addAll(
                titleLabel,
                emailLabel,
                permsPanel,
                authButton,
                createAccountButton,
                notificationPane
        );
    }

    /**
     * Set buttons' action
     */
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

    /**
     * Reloads all the UI
     */
    public void reload() {
        updateTitle();
        updateEmail();
        updatePerms();
        updateButton();
    }

    /**
     * Update the title<br>
     * If {@linkplain ConnectionHandler#isLogged}, also displays the first and last name
     */
    private void updateTitle() {
        if (isLogged)
            titleLabel.setText(Functions.formatMessage(
                    Translations.get("potoflux:tabs.account.title.connected"),
                    account.firstName, account.lastName
            ));
        else titleLabel.setText(Translations.get("potoflux:tabs.account.title"));
    }

    /**
     * Updates the email with the connected user's mails and controls if rendered
     */
    private void updateEmail() {
        if (isLogged) {
            emailLabel.setVisible(true);
            emailLabel.setText(account.email);
        }
        else emailLabel.setVisible(false);
    }

    /**
     * Update the title, showing or hiding it, also add only perms owned by the connected user
     */
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

    /**
     * Update all buttons, changing the names or render state
     */
    private void updateButton() {
        authButton.setText(getAuthButtonStatus());
        createAccountButton.setVisible(!isLogged);
        createAccountButton.setDisable(!isAccountCreationEnabled);
    }

    /**
     * Disables the preset
     * @return {@code false}
     */
    @Override
    protected boolean doPreset() {
        return false;
    }
}
