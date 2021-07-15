package tech.peterj.coinpamp.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestController
public class AppController {

    private static final Logger LOGGER = Logger.getLogger(AppController.class.getName());

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        LOGGER.info("REST request health check");
        return new ResponseEntity<>("{\"status\" : \"UP\"}", HttpStatus.OK);
    }

    @GetMapping("/{sub}/topTen")
    Map<String, Object> getTopTenPostsInSub(@PathVariable String sub) throws IOException, InterruptedException {
        Map<String, Object> map = new HashMap<>();

        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(
                URI.create("https://www.reddit.com/r/CryptoCurrency/hot/.json?count=1"))
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = null;
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        LOGGER.info(String.valueOf(response.statusCode()));
        // LOGGER.info(response.body());

        ObjectMapper om = new ObjectMapper();
        JsonNode json = json = om.readTree(response.body());

        JsonNode posts = json.get("data").get("children");
        JsonNode postData = posts.get(0).get("data");

        String title = postData.get("title").asText();
        String author_fullname = postData.get("author_fullname").asText();
        String selfText = postData.get("selftext").asText();
        String ups = postData.get("ups").asText();
        long timestampUnix = postData.get("created").asLong();

        LOGGER.info(String.valueOf(timestampUnix));

        Timestamp createdTs = Timestamp.from(Instant.ofEpochSecond(timestampUnix));
        LocalDateTime created = createdTs.toLocalDateTime();

        LOGGER.info("title: " + title + ", ups: " + ups + ", created: " + created);

        return map;
    }

}
