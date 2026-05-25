package net.minheur.potoflux.screen.tabs;

import javafx.geometry.Side;
import net.minheur.potoflux.settings.types.IComboSetting;
import net.minheur.potoflux.translations.Translations;
import org.jetbrains.annotations.Nullable;

public enum TabSides implements IComboSetting {
    TOP(Side.TOP, "top", "common:top"),
    BOTTOM(Side.BOTTOM, "bottom", "common:bottom"),
    LEFT(Side.LEFT, "left", "common:left"),
    RIGHT(Side.RIGHT, "right", "common:right");

    private final Side side;
    private final String code;
    private final String translatableDisplay;

    TabSides(Side side, String code, String translatableDisplay) {
        this.side = side;
        this.code = code;
        this.translatableDisplay = translatableDisplay;
    }

    @Override
    public String returnValue() {
        return code;
    }

    public Side getSide() {
        return side;
    }

    public static @Nullable TabSides getFromCode(String code) {
        for (TabSides side : TabSides.values())
            if (code.equals(side.code)) return side;
        return null;
    }

    @Override
    public String toString() {
        return Translations.get(translatableDisplay);
    }
}
