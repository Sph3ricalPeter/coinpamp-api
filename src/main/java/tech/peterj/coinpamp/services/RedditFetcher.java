package tech.peterj.coinpamp.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tech.peterj.coinpamp.model.RedditPost;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class RedditFetcher {

    private static final Logger LOGGER = Logger.getLogger(RedditFetcher.class.getName());

    private final HttpClient client = HttpClient.newHttpClient();
    private final RedditPostService service;

    public RedditFetcher(RedditPostService service) {
        this.service = service;
    }

    private HttpResponse<String> fetch(String url) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder(
                URI.create(url))
                .header("Accept", "application/json")
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private List<RedditPost> getNLatestPostsFromResponse(HttpResponse<String> response, int n) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        JsonNode json = om.readTree(response.body());

        if (json == null) {
            return new ArrayList<>();
        }

        JsonNode postNodes = json.get("data").get("children");

        int postCount = Math.min(n, postNodes.size());
        List<RedditPost> posts = new ArrayList<>();

        for (int i = 0; i < postCount; ++i) {
            JsonNode postData = postNodes.get(i).get("data");

            posts.add(new RedditPost(
                    postData.get("id").asText(),
                    postData.get("title").asText(),
                    postData.get("author_fullname").asText(),
                    postData.get("selftext").asText(),
                    postData.get("ups").asText(),
                    Timestamp.from(Instant.ofEpochSecond(postData.get("created").asLong())),
                    postData.get("permalink").asText()
            ));
        }

        return posts;
    }

    // @Scheduled(fixedDelay = 1000) // fetch every 5 minutes: fixedDelay = 300000
    public void fetch() {
        LOGGER.info("fetching ...");
    }

    @Transactional
    public void fetchHotCrypto(int nPosts) throws IOException, InterruptedException {
        HttpResponse<String> response = fetch("https://www.reddit.com/r/CryptoCurrency/hot/.json");

        if (response == null) {
            return;
        }

        List<RedditPost> posts = getNLatestPostsFromResponse(response, nPosts);
        service.saveAllPosts(posts);
    }

}
