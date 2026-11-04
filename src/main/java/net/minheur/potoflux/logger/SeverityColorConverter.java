package net.minheur.potoflux.logger;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase;
import org.jetbrains.annotations.NotNull;

public class SeverityColorConverter extends ForegroundCompositeConverterBase<ILoggingEvent> {

    @Override
    protected String getForegroundColorCode(@NotNull ILoggingEvent event) {
        Level level = event.getLevel();
        if (level.isGreaterOrEqual(Level.ERROR)) return "31";
        if (level.isGreaterOrEqual(Level.WARN)) return "38;2;196;170;3";
        return "32";
    }
}
