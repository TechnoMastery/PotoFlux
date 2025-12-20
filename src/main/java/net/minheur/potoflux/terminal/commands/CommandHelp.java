package net.minheur.potoflux.terminal.commands;

import net.minheur.potoflux.translations.Translations;

public class CommandHelp {
    private static final String tab = "    ";
    private static final String tabLine = "\n" + tab;

    static String helloWorld() {
        return Translations.get("potoflux:command.hello_world") + tabLine +
                Translations.get("potoflux:command.hello_world.use");
    }
    static String clear() {
        return Translations.get("potoflux:command.clear") + tabLine +
                Translations.get("potoflux:command.clear.use");
    }
    static String time() {
        return Translations.get("potoflux:command.time") + tabLine +
                Translations.get("potoflux:command.time.use");
    }
    static String help() {
        return Translations.get("potoflux:command.help") + tabLine +
                Translations.get("potoflux:command.help.use") + tabLine +
                Translations.get("potoflux:command.help.use.command");
    }
    static String echo() {
        return Translations.get("potoflux:command.echo") + tabLine +
                Translations.get("potoflux:command.echo.use");
    }
    static String tab() {
        return Translations.get("potoflux:command.tab") + tabLine +
                Translations.get("potoflux:command.tab.use");
    }
    static String source() {
        return Translations.get("potoflux:command.source") + tabLine +
                Translations.get("potoflux:command.source.use");
    }
    static String ascii() {
        return Translations.get("potoflux:command.ascii") + tabLine +
                Translations.get("potoflux:command.ascii.use") + tabLine +
                Translations.get("potoflux:command.ascii.use.choose");
    }
    static String quit() {
        return Translations.get("potoflux:command.quit") + tabLine +
                Translations.get("potoflux:command.quit.use");
    }
}
