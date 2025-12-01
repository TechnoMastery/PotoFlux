package net.minheur.potoflux.terminal.commands;


import net.minheur.potoflux.translations.Translations;

public class CommandHelp {
    private static final String tab = "    ";
    private static final String tabLine = "\n" + tab;

    static String helloWorld() {
        return Translations.get("command.hello_world") + tabLine +
                Translations.get("command.hello_world.use");
    }
    static String clear() {
        return Translations.get("command.clear") + tabLine +
                Translations.get("command.clear.use");
    }
    static String time() {
        return Translations.get("command.time") + tabLine +
                Translations.get("command.time.use");
    }
    static String help() {
        return Translations.get("command.help") + tabLine +
                Translations.get("command.help.use") + tabLine +
                Translations.get("command.help.use.command");
    }
    static String echo() {
        return Translations.get("command.echo") + tabLine +
                Translations.get("command.echo.use");
    }
    static String tab() {
        return Translations.get("command.tab") + tabLine +
                Translations.get("command.tab.use");
    }
    static String source() {
        return Translations.get("command.source") + tabLine +
                Translations.get("command.source.use");
    }
    static String ascii() {
        return Translations.get("command.ascii") + tabLine +
                Translations.get("command.ascii.use") + tabLine +
                Translations.get("command.ascii.use.choose");
    }
    static String quit() {
        return Translations.get("command.quit") + tabLine +
                Translations.get("command.quit.use");
    }
}
