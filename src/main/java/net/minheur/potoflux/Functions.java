package net.minheur.potoflux;

import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Functions {
    public static void exit(int delay, int status) {
        Timer exitDelay = new Timer(delay, ev -> PotoFlux.runProgramClosing(status));
        exitDelay.setRepeats(false);
        exitDelay.start();
    }

    public static String removeProhibitedChar(String s) {
        if (s == null) return null;
        return s.replaceAll("[^\\p{L}\\p{N} @!,.?:;*$+/→\\-^'¨()#§]+", "");
    }
    public static String escapeHtml(String s) {
        if (s == null) return null;
        return s.replace("&", "")
                .replace("<", "")
                .replace(">", "");
    }

    public static List<String> listResourceFiles(String folder) throws IOException {
        List<String> result = new ArrayList<>();

        URL dirURL = Functions.class.getClassLoader().getResource(folder);
        if (dirURL == null) return result;

        if (dirURL.getProtocol().equals("file")) { // IDE
            try {
                Path dirPath = Paths.get(dirURL.toURI());
                try (var paths = Files.walk(dirPath)) {
                    paths.filter(Files::isRegularFile)
                            .filter(p -> p.toString().endsWith(".txt"))
                            .forEach(p -> result.add(folder + "/" + p.getFileName().toString()));
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else if (dirURL.getProtocol().equals("jar")) { // JAR
            String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!"));
            try (JarFile jar = new JarFile(URLDecoder.decode(jarPath, StandardCharsets.UTF_8))) {
                Enumeration<JarEntry> entries = jar.entries();

                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName();

                    if (name.startsWith(folder + "/") && name.endsWith(".txt")) result.add(name);
                }
            }
        }
        return result;
    }
}
