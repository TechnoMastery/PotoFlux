package net.minheur.potoflux.utils;

import net.minheur.potoflux.logger.PtfLogger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class OnlineReader {
    public static String read(String url) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) throw new RuntimeException("HTTP " + response.statusCode());

            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            PtfLogger.error("Could not get online String for URL: " + url);
            return null;
        }
    }
}
