package tech.peterj.coinpamp.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;

@Service
public class Fetcher {

    private static final Logger LOGGER = Logger.getLogger(RedditFetcher.class.getName());
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper om = new ObjectMapper();

    public static JsonNode fetch(String baseUrl, String path, String query) throws IOException, InterruptedException {
        var url = baseUrl + path + query;
        var request = HttpRequest.newBuilder(
                URI.create(url))
                .header("Accept", "application/json")
                .build();

        LOGGER.info("fetched data from " + url);

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        var jsonTree = om.readTree(response.body());

        return jsonTree;
    }

}
