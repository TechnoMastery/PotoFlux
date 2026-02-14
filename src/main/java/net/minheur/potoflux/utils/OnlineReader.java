package net.minheur.potoflux.utils;

import net.minheur.potoflux.logger.PtfLogger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Utility class to read online files.
 */
public class OnlineReader {
    /**
     * Reads the content of an online file as a string
     * @param url the address of the file
     * @return the content of the online file
     */
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
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }

    private static HttpRequest getHttpRequest(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        return request;
    }
}
