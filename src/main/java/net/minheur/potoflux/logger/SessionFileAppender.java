package net.minheur.potoflux.logger;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class SessionFileAppender extends FileAppender<ILoggingEvent> {
    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-SSS");

    private String sessionTimestamp;
    private boolean archived;

    private boolean archiveOnStop = true;

    public void disableSaving() {
        archiveOnStop = false;
    }

    @Override
    public void start() {
        sessionTimestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        archived = false;
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        if (!archiveOnStop || archived || sessionTimestamp == null || getFile() == null) return;
        archived = true;

        Path logFile = Path.of(getFile());
        Path archiveDirectory = logFile.toAbsolutePath().getParent();
        Path temporaryLogFile = archiveDirectory.resolve(sessionTimestamp + ".log");
        Path archiveFile = archiveDirectory.resolve(sessionTimestamp + ".zip");

        try {
            if (!Files.exists(logFile)) return;
            Files.copy(logFile, temporaryLogFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            try (
                    OutputStream output = Files.newOutputStream(archiveFile);
                    ZipOutputStream zip = new ZipOutputStream(output)
            ) {
                zip.putNextEntry(new ZipEntry(temporaryLogFile.getFileName().toString()));
                Files.copy(temporaryLogFile, zip);
                zip.closeEntry();
            }
        } catch (IOException e) {
            addError("Failed to archive the log file", e);
        } finally {
            try {
                Files.deleteIfExists(temporaryLogFile);
            } catch (IOException e) {
                addError("Failed to delete the temporary log file", e);
            }
        }
    }
}
