package net.minheur.potoflux.terminal.commands;


import net.minheur.potoflux.translations.TranslationsOld;

public class CommandHelp {
    private static final String tab = "    ";
    private static final String tabLine = "\n" + tab;

    static String helloWorld() {
        return TranslationsOld.get("command.hello_world") + tabLine +
                TranslationsOld.get("command.hello_world.use");
    }
    static String clear() {
        return TranslationsOld.get("command.clear") + tabLine +
                TranslationsOld.get("command.clear.use");
    }
    static String time() {
        return TranslationsOld.get("command.time") + tabLine +
                TranslationsOld.get("command.time.use");
    }
    static String help() {
        return TranslationsOld.get("command.help") + tabLine +
                TranslationsOld.get("command.help.use") + tabLine +
                TranslationsOld.get("command.help.use.command");
    }
    static String echo() {
        return TranslationsOld.get("command.echo") + tabLine +
                TranslationsOld.get("command.echo.use");
    }
    static String tab() {
        return TranslationsOld.get("command.tab") + tabLine +
                TranslationsOld.get("command.tab.use");
    }
    static String source() {
        return TranslationsOld.get("command.source") + tabLine +
                TranslationsOld.get("command.source.use");
    }
    static String ascii() {
        return TranslationsOld.get("command.ascii") + tabLine +
                TranslationsOld.get("command.ascii.use") + tabLine +
                TranslationsOld.get("command.ascii.use.choose");
    }
    static String quit() {
        return TranslationsOld.get("command.quit") + tabLine +
                TranslationsOld.get("command.quit.use");
    }
}
