package net.minheur.potoflux.terminal.commands;

import net.minheur.potoflux.translations.Translations;
import org.jetbrains.annotations.NotNull;

import static net.minheur.potoflux.terminal.commands.CommandMakerHelpers.tabLine;

/**
 * The class containing all potoflux command helps
 */
public class CommandHelp {
    /**
     * The help for the command {@code hello}.
     *
     * @return the help for {@code hello}.
     */
    static @NotNull String helloWorld() {
        return Translations.get("potoflux:command.hello_world") + tabLine +
                Translations.get("potoflux:command.hello_world.use");
    }

    /**
     * The help for the command {@code clear}.
     *
     * @return the help for {@code clear}.
     */
    static @NotNull String clear() {
        return Translations.get("potoflux:command.clear") + tabLine +
                Translations.get("potoflux:command.clear.use");
    }

    /**
     * The help for the command {@code time}.
     *
     * @return the help for {@code time}.
     */
    static @NotNull String time() {
        return Translations.get("potoflux:command.time") + tabLine +
                Translations.get("potoflux:command.time.use");
    }

    /**
     * The help for the command {@code help}.
     *
     * @return the help for {@code help}.
     */
    static @NotNull String help() {
        return Translations.get("potoflux:command.help") + tabLine +
                Translations.get("potoflux:command.help.use") + tabLine +
                Translations.get("potoflux:command.help.use.command");
    }

    /**
     * The help for the command {@code echo}.
     *
     * @return the help for {@code echo}.
     */
    static @NotNull String echo() {
        return Translations.get("potoflux:command.echo") + tabLine +
                Translations.get("potoflux:command.echo.use");
    }

    /**
     * The help for the command {@code tab}.
     *
     * @return the help for {@code tab}.
     */
    static @NotNull String tab() {
        return Translations.get("potoflux:command.tab") + tabLine +
                Translations.get("potoflux:command.tab.use");
    }

    /**
     * The help for the command {@code source}.
     *
     * @return the help for {@code source}.
     */
    static @NotNull String source() {
        return Translations.get("potoflux:command.source") + tabLine +
                Translations.get("potoflux:command.source.use");
    }

    /**
     * The help for the command {@code ascii}.
     *
     * @return the help for {@code ascii}.
     */
    static @NotNull String ascii() {
        return Translations.get("potoflux:command.ascii") + tabLine +
                Translations.get("potoflux:command.ascii.use") + tabLine +
                Translations.get("potoflux:command.ascii.use.choose");
    }

    /**
     * The help for the command {@code quit}.
     *
     * @return the help for {@code quit}.
     */
    static @NotNull String quit() {
        return Translations.get("potoflux:command.quit") + tabLine +
                Translations.get("potoflux:command.quit.use");
    }

    /**
     * The help for the command {@code modList}.
     *
     * @return the help for {@code modList}.
     */
    static @NotNull String modList() {
        return Translations.get("potoflux:command.modList") + tabLine +
                Translations.get("potoflux:command.modList.use");
    }

    /**
     * The help for the command {@code modDir}.
     *
     * @return the help for {@code modDir}.
     */
    static @NotNull String modDir() {
        return Translations.get("potoflux:command.modDir") + tabLine +
                Translations.get("potoflux:command.modDir.use");
    }

    static @NotNull String logDir() {
        return Translations.get("potoflux:command.logDir") + tabLine +
                Translations.get("potoflux:command.logDir.use");
    }

    /**
     * The help for the command {@code ptfVersion}.
     *
     * @return the help for {@code ptfVersion}.
     */
    static @NotNull String ptfVersion() {
        return Translations.get("potoflux:command.version") + tabLine +
                Translations.get("potoflux:command.version.use");
    }

    /**
     * The help for the command {@code tabList}.
     *
     * @return the help for {@code tabList}.
     */
    static @NotNull String tabList() {
        return Translations.get("potoflux:command.listTab") + tabLine +
                Translations.get("potoflux:command.listTab.use") + tabLine +
                Translations.get("potoflux:command.listTab.use.resourceLoc");
    }
}
