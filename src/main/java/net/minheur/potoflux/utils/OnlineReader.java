package net.minheur.potoflux.utils;

import net.minheur.potoflux.logger.PtfLogger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Utility object to read online files.
 * Provides Java-friendly static API: OnlineReader.read(url)
 */
public final class OnlineReader {

    private OnlineReader() {}

    public static String read(String url) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = getHttpRequest(url);
            HttpResponse<String> response = getHttpResponse(client, request);
            if (response.statusCode() != 200) throw new RuntimeException("HTTP " + response.statusCode());
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            PtfLogger.error("Could not get online String for URL: " + url);
            return null;
        }
    }

    private static HttpResponse<String> getHttpResponse(HttpClient client, HttpRequest request) throws IOException, InterruptedException {
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static HttpRequest getHttpRequest(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
    }
}
