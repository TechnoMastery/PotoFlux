package net.minheur.potoflux.screen.tabs.all;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.screen.tabs.BaseVTab;
import net.minheur.potoflux.settings.Setting;
import net.minheur.potoflux.settings.SettingInfo;
import net.minheur.potoflux.settings.SettingRegistry;
import net.minheur.potoflux.settings.types.ISettingType;
import net.minheur.potoflux.settings.types.PreferencesTypes;
import net.minheur.potoflux.translations.Translations;
import net.minheur.potoflux.settings.UserPrefsManager;

import java.util.*;

/**
 * The settings tab contains all user changeable vars (prefs)
 */
public class SettingsTab extends BaseVTab<VBox> {

    private Map<Setting, SettingInfo<?>> settings;

    private ScrollPane contentScroll;

    private Button apply;
    private Button cancel;

    /**
     * This is the actual method to set the panel.<br>
     * The overriding class will have to use this to add content to the {@link #PANEL}.
     */
    @Override
    protected void setPanel() {
        // todo: move all below to Setting class (using reg)
        // addLangCombo(); todo lang chooser
        // addAsciiCombo(); todo ascii chooser
        // addThemeCombo(); todo theme chooser
        // todo ascii on start checkbox
        // todo tab placement chooser

        // todo optional property system

        settings = new HashMap<>();

        fillSetting();
        buildLines();

        addButtons();
    }

    private void fillSetting() {

        settings.clear();
        List<Setting> allSettings = SettingRegistry.getAll().stream()
                .sorted(Comparator.comparing(
                        tab -> !tab.id().getNamespace().equals(PotoFlux.ID)
                ))
                .toList();

        for (Setting setting : allSettings) {

            ISettingType<?> type = setting.type();
            SettingInfo<?> info = new SettingInfo<>(type.prefType());

            Object value = UserPrefsManager.getValueFor(type, setting.id());
            info.setActualValue(value);
            type.selectValue(value);

            settings.put(setting, info);

        }

    }

    private void buildLines() {

        vContent.getChildren().clear();

        for (Map.Entry<Setting, SettingInfo<?>> entry : settings.entrySet()) {

            HBox pane = new HBox(5);
            pane.setPadding(new Insets(0, 0, 0, 10));
            Label modified = entry.getKey().type().getIsModifiedLabel();
            modified.setStyle("-fx-font-weight: bold;");
            Node node = entry.getKey().type().getExecutionNode();
            pane.getChildren().addAll(modified, node);

            modified.textProperty().bind(
                    Bindings.createStringBinding(() ->
                            Objects.equals(
                                    entry.getKey().type().getSelectedValue(),
                                    entry.getValue().getActualValue()
                            ) ? "" : "!",
                            entry.getKey().type().valueProperty()
                    )
            );

            vContent.getChildren().add(pane);

        }

    }

    @Override
    protected void instantiate() {
        PANEL = new VBox();
        PANEL.setAlignment(Pos.TOP_CENTER);
        PANEL.setSpacing(20);

        boxPreset();

        Label title = mkTitle();
        title.setPadding(new Insets(20, 0, 0, 0));

        PANEL.getChildren().addAll(title, contentScroll);
    }

    @Override
    protected void boxPreset() {
        vContent = new VBox();
        vContent.setSpacing(20);
        vContent.setPadding(new Insets(15, 0, 15, 0));
        vContent.setAlignment(Pos.TOP_CENTER);

        contentScroll =  new ScrollPane(vContent);
        contentScroll.setFitToWidth(true);
        contentScroll.setFitToHeight(false);
        contentScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        contentScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        VBox.setVgrow(contentScroll, Priority.ALWAYS);
    }

    private void addButtons() {

        cancel = new Button(Translations.get("common:cancel"));
        apply = new Button(Translations.get("common:apply"));

        setupAction();

        HBox buttons = new HBox(10, cancel, apply);
        buttons.setPadding(new Insets(10));
        buttons.setAlignment(Pos.CENTER_RIGHT);

        PANEL.getChildren().add(buttons);
    }

    private void setupAction() {
        apply.setDefaultButton(true);

        apply.setOnAction(e -> {

            Map<Setting, SettingInfo<?>> modifiedSettings = new HashMap<>();
            boolean requireRestart = false;

            for (Map.Entry<Setting, SettingInfo<?>> entry : settings.entrySet()) {
                // check if changed
                if (Objects.equals(
                        entry.getKey().type().getSelectedValue(),
                        entry.getValue().getActualValue()
                )) continue;

                Object newValue = entry.getKey().type().getSelectedValue();
                entry.getValue().setActualValue(newValue);

                modifiedSettings.put(
                        entry.getKey(),
                        entry.getValue()
                );
                if (entry.getKey().requireRestart()) requireRestart = true;

            }

            for (Map.Entry<Setting, SettingInfo<?>> entry : modifiedSettings.entrySet())
                UserPrefsManager.setValueFor(
                        entry.getKey().id(),
                        entry.getKey().type().prefType(),
                        entry.getValue().getActualValue()
                );

            if (requireRestart) UserPrefsManager.showReload();
            fillSetting();

        });

        cancel.setOnAction(e -> {

            for (Map.Entry<Setting, SettingInfo<?>> entry : settings.entrySet()) {
                // check if changed
                if (Objects.equals(
                        entry.getKey().type().getSelectedValue(),
                        entry.getValue().getActualValue()
                )) continue;

                Object actualValue = entry.getValue().getActualValue();
                entry.getKey().type().selectValue(
                        actualValue == null ?
                                entry.getKey().type().getDefaultValue() :
                                actualValue
                );

            }

        });
    }

    /**
     * Adds the button to change the terminal's ASCII
     */
    private void addAsciiCombo() {
        Button asciiButton = new Button(Translations.get("potoflux:prefs.ascii.button"));
        asciiButton.setOnAction(e -> {
            UserPrefsManager.resetTerminalAscii();
        });
        asciiButton.setFont(Font.font("Consolas", 15));
        // asciiButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        vContent.getChildren().add(asciiButton);
    }

    /**
     * Adds the button to change the app's lang
     */
    private void addLangCombo() {
        Button langButton = new Button(Translations.get("potoflux:prefs.lang.button"));
        langButton.setOnAction(e -> {
            UserPrefsManager.resetUserLang();
        });
        langButton.setFont(Font.font("Consolas", 15));
        // langButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        vContent.getChildren().add(langButton);
    }

    /**
     * Adds the button to change the theme
     */
    private void addThemeCombo() {
        Button themeButton = new Button(Translations.get("potoflux:prefs.theme.button"));
        themeButton.setOnAction(e -> {
            UserPrefsManager.resetTheme();
        });
        themeButton.setFont(Font.font("Consolas", 15));
        // themeButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        vContent.getChildren().add(themeButton);
    }

    /**
     * Used to set the title.
     * @return the title of the tab.
     */
    @Override
    protected String getTitle() {
        return Translations.get("potoflux:tabs.settings.name");
    }
    @Override
    protected String getName() {
        return getTitle(); // same
    }

    @Override
    protected boolean doPreset() {
        return false;
    }
}
