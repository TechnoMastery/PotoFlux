package net.minheur.potoflux.screen.tabs;

import javafx.geometry.Side;
import net.minheur.potoflux.settings.types.IComboSetting;
import net.minheur.potoflux.translations.Translations;
import org.jetbrains.annotations.Nullable;

/**
 * Enum extending {@link Side}, also featuring a key and translated name
 */
public enum TabSides implements IComboSetting {
    /**
     * {@link Side#TOP}
     */
    TOP(Side.TOP, "top", "common:top"),
    /**
     * {@link Side#BOTTOM}
     */
    BOTTOM(Side.BOTTOM, "bottom", "common:bottom"),
    /**
     * {@link Side#LEFT}
     */
    LEFT(Side.LEFT, "left", "common:left"),
    /**
     * {@link Side#RIGHT}
     */
    RIGHT(Side.RIGHT, "right", "common:right");

    /**
     * Actual {@link Side} of the value
     */
    private final Side side;
    /**
     * Code for the side stored in the prefs
     */
    private final String code;
    /**
     * Translation key used for the name
     */
    private final String translatableDisplay;

    /**
     * Constructor of the side
     * @param side real side
     * @param code stored in the prefs
     * @param translatableDisplay translation key used for the name
     */
    TabSides(Side side, String code, String translatableDisplay) {
        this.side = side;
        this.code = code;
        this.translatableDisplay = translatableDisplay;
    }

    /**
     * Getter for the {@link #code}
     * @return {@link #code}
     */
    @Override
    public String returnValue() {
        return code;
    }

    /**
     * Getter for the real side
     * @return {@link #side}
     */
    public Side getSide() {
        return side;
    }

    /**
     * Static getter for a {@link TabSides} from a code
     * @param code to get the side of
     * @return the corresponding side, or {@code null} if not exists
     */
    public static @Nullable TabSides getFromCode(String code) {
        for (TabSides side : TabSides.values())
            if (code.equals(side.code)) return side;
        return null;
    }

    /**
     * Make to string gets the translated name
     * @return the translated name
     */
    @Override
    public String toString() {
        return Translations.get(translatableDisplay);
    }
}
