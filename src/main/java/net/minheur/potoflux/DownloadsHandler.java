package net.minheur.potoflux;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class DownloadsHandler {
    public static final String modGenURL = "https://technomastery.github.io/PotoFLuxAppData/modGenData/";

    public static void downloadToFile(String url, File destination) throws IOException {
        destination.getParentFile().mkdirs();

        try (InputStream in = new URL(url).openStream()) {
            Files.copy(in, destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
