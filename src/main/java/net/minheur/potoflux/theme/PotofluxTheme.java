package net.minheur.potoflux.theme;

import atlantafx.base.theme.Theme;
import net.minheur.potoflux.registry.IRegistryType;
import net.minheur.potoflux.settings.types.IComboSetting;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class PotofluxTheme implements IComboSetting, IRegistryType, Theme {

    private final @NotNull ResourceLocation id;
    private final @NotNull String displayName;
    private final @NotNull String userAgentStylesheetPath;
    private final @Nullable String userAgentStylesheetBSSPath;
    private final boolean isDarkMod;

    public PotofluxTheme(@NotNull ResourceLocation id, @NotNull String displayName,
                         @NotNull String userAgentStylesheetPath, @Nullable String userAgentStylesheetBSSPath, boolean isDarkMod) {
        this.id = id;
        this.displayName = displayName;
        this.userAgentStylesheetPath = userAgentStylesheetPath;
        this.userAgentStylesheetBSSPath = userAgentStylesheetBSSPath;
        this.isDarkMod = isDarkMod;
    }
    public PotofluxTheme(@NotNull ResourceLocation id, @NotNull String displayName,
                         @NotNull String userAgentStylesheetPath, boolean isDarkMod) {
        this.id = id;
        this.displayName = displayName;
        this.userAgentStylesheetPath = userAgentStylesheetPath;
        this.userAgentStylesheetBSSPath = null;
        this.isDarkMod = isDarkMod;
    }
    public PotofluxTheme(@NotNull ResourceLocation id, @NotNull String displayName, @NotNull Theme parent) {
        this.id = id;
        this.displayName = displayName;
        this.userAgentStylesheetPath = parent.getUserAgentStylesheet();
        this.userAgentStylesheetBSSPath = parent.getUserAgentStylesheetBSS();
        this.isDarkMod = parent.isDarkMode();
    }

    @Override
    public String returnValue() {
        return id.toString();
    }

    @Override
    public String getName() {
        return id.getPath();
    }

    @Override
    public String getUserAgentStylesheet() {
        return userAgentStylesheetPath;
    }

    @Override
    public @Nullable String getUserAgentStylesheetBSS() {
        return userAgentStylesheetBSSPath;
    }

    public String getOptimalUserAgentStylesheet() {
        return Objects.requireNonNullElse(userAgentStylesheetBSSPath, userAgentStylesheetPath);
    }

    @Override
    public boolean isDarkMode() {
        return isDarkMod;
    }

    @Override
    public ResourceLocation id() {
        return id;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
