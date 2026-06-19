package net.minheur.potoflux.utils

import net.minheur.potoflux.logger.PtfLogger
import java.io.IOException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

/**
 * Utility object to read online files.
 * Provides Java-friendly static API: OnlineReader.read(url)
 */
object OnlineReader {
    @JvmStatic
    fun read(url: String): String? {
        return try {
            val client = HttpClient.newHttpClient()
            val request = getHttpRequest(url)
            val response = getHttpResponse(client, request)
            if (response.statusCode() != 200) throw RuntimeException("HTTP " + response.statusCode())
            response.body()
        } catch (e: Exception) {
            e.printStackTrace()
            PtfLogger.error("Could not get online String for URL: $url")
            null
        }
    }

    @Throws(IOException::class, InterruptedException::class)
    private fun getHttpResponse(client: HttpClient, request: HttpRequest): HttpResponse<String> {
        return client.send(request, HttpResponse.BodyHandlers.ofString())
    }

    private fun getHttpRequest(url: String): HttpRequest {
        return HttpRequest.newBuilder()
            .uri(URI.create(url))
            .GET()
            .build()
    }
}
