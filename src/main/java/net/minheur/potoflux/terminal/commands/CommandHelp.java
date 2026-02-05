package net.minheur.potoflux.terminal.commands;

import net.minheur.potoflux.translations.Translations;

/**
 * The class containing all potoflux command helps
 */
public class CommandHelp {
    /**
     * A contant for the tabulation
     */
    private static final String tab = "    ";
    /**
     * A constant for a tabulation, preceded by a new line.
     */
    private static final String tabLine = "\n" + tab;

    /**
     * The help for the command {@code hello}.
     * @return the help for {@code hello}.
     */
    static String helloWorld() {
        return Translations.get("potoflux:command.hello_world") + tabLine +
                Translations.get("potoflux:command.hello_world.use");
    }
    /**
     * The help for the command {@code clear}.
     * @return the help for {@code clear}.
     */
    static String clear() {
        return Translations.get("potoflux:command.clear") + tabLine +
                Translations.get("potoflux:command.clear.use");
    }
    /**
     * The help for the command {@code time}.
     * @return the help for {@code time}.
     */
    static String time() {
        return Translations.get("potoflux:command.time") + tabLine +
                Translations.get("potoflux:command.time.use");
    }
    /**
     * The help for the command {@code help}.
     * @return the help for {@code help}.
     */
    static String help() {
        return Translations.get("potoflux:command.help") + tabLine +
                Translations.get("potoflux:command.help.use") + tabLine +
                Translations.get("potoflux:command.help.use.command");
    }
    /**
     * The help for the command {@code echo}.
     * @return the help for {@code echo}.
     */
    static String echo() {
        return Translations.get("potoflux:command.echo") + tabLine +
                Translations.get("potoflux:command.echo.use");
    }
    /**
     * The help for the command {@code tab}.
     * @return the help for {@code tab}.
     */
    static String tab() {
        return Translations.get("potoflux:command.tab") + tabLine +
                Translations.get("potoflux:command.tab.use");
    }
    /**
     * The help for the command {@code source}.
     * @return the help for {@code source}.
     */
    static String source() {
        return Translations.get("potoflux:command.source") + tabLine +
                Translations.get("potoflux:command.source.use");
    }
    /**
     * The help for the command {@code ascii}.
     * @return the help for {@code ascii}.
     */
    static String ascii() {
        return Translations.get("potoflux:command.ascii") + tabLine +
                Translations.get("potoflux:command.ascii.use") + tabLine +
                Translations.get("potoflux:command.ascii.use.choose");
    }
    /**
     * The help for the command {@code quit}.
     * @return the help for {@code quit}.
     */
    static String quit() {
        return Translations.get("potoflux:command.quit") + tabLine +
                Translations.get("potoflux:command.quit.use");
    }
    /**
     * The help for the command {@code modList}.
     * @return the help for {@code modList}.
     */
    static String modList() {
        return Translations.get("potoflux:command.modList") + tabLine +
                Translations.get("potoflux:command.modList.use");
    }
    /**
     * The help for the command {@code modDir}.
     * @return the help for {@code modDir}.
     */
    static String modDir() {
        return Translations.get("potoflux:command.modDir") + tabLine +
                Translations.get("potoflux:command.modDir.use");
    }
}
