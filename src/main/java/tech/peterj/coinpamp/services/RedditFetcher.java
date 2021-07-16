package tech.peterj.coinpamp.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private static final String REDDIT_BASE_URL = "https://www.reddit.com";

    private final HttpClient client = HttpClient.newHttpClient();
    private final RedditPostService service;

    public RedditFetcher(RedditPostService service) {
        this.service = service;
    }

    private HttpResponse<String> fetchReddit(String path, String query) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder(
                URI.create(REDDIT_BASE_URL + path + query))
                .header("Accept", "application/json")
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private List<RedditPost> readRedditPostsFromResponse(HttpResponse<String> response) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        JsonNode json = om.readTree(response.body());

        if (json == null) {
            return new ArrayList<>();
        }

        JsonNode postNodes = json.get("data").get("children");

        int nPosts = postNodes.size();
        List<RedditPost> posts = new ArrayList<>();

        LOGGER.info("fetched " + nPosts + " posts");

        for (int i = 0; i < nPosts; ++i) {
            JsonNode postData = postNodes.get(i).get("data");

            posts.add(new RedditPost(
                    postData.get("id").asText(),
                    postData.get("title").asText(),
                    postData.get("author_fullname").asText(),
                    "", // postData.get("selftext").asText(),
                    postData.get("ups").asText(),
                    Timestamp.from(Instant.ofEpochSecond(postData.get("created").asLong())),
                    postData.get("permalink").asText()
            ));
        }

        return posts;
    }

    // @Scheduled(fixedDelay = 1000) // fetch every 5 minutes: fixedDelay = 300000
    public void doScheduledFetch() {
        LOGGER.info("fetching ...");
    }

    @Transactional
    public void fetchLatestNCryptoPosts(int n) throws IOException, InterruptedException {
        // fixme: move the url parameters to the request builder ..
        // todo: this should also be all posts within last 24 hrs (or different interval)
        // also ideally only show the ones that have the most upvotes
        String path = "/r/CryptoCurrency/new.json";
        String query = String.format("?sort=new&limit=%d", n);

        HttpResponse<String> response = fetchReddit(path, query);
        if (response == null) {
            return;
        }

        List<RedditPost> posts = readRedditPostsFromResponse(response);
        service.saveAllPosts(posts);
    }

}
