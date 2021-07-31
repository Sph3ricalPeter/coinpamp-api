package tech.peterj.coinpamp.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import tech.peterj.coinpamp.model.RedditPost;

import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@Service
public class RedditFetcher {

    private static final Logger LOGGER = Logger.getLogger(RedditFetcher.class.getName());
    private static final String REDDIT_BASE_URL = "https://www.reddit.com";

    private final RedditPostService service;

    public RedditFetcher(RedditPostService service) {
        this.service = service;
    }

    private List<RedditPost> readRedditPostsFromJsonTree(JsonNode root) throws JsonProcessingException {
        Objects.requireNonNull(root);

        JsonNode postNodes = root.get("data").get("children");

        List<RedditPost> posts = new ArrayList<>();
        postNodes.forEach(postNode -> {
            var dataNode = postNode.get("data");
            posts.add(new RedditPost(
                    dataNode.get("id").asText(),
                    dataNode.get("title").asText(),
                    dataNode.get("author_fullname").asText(),
                    dataNode.get("selftext").asText(),
                    dataNode.get("ups").asText(),
                    Timestamp.from(Instant.ofEpochSecond(dataNode.get("created").asLong())),
                    dataNode.get("permalink").asText()
            ));
        });

        LOGGER.info("fetched " + posts.size() + " posts");

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
        var redditPostRoot = Fetcher.fetch(REDDIT_BASE_URL, "/r/CryptoCurrency/new.json", String.format("?sort=new&limit=%d", n));
        List<RedditPost> posts = readRedditPostsFromJsonTree(redditPostRoot);

        service.saveAllPosts(posts);
    }

    public void fetchTopNCryptoPosts(int n) throws IOException, InterruptedException {
        var redditPostRoot = Fetcher.fetch(REDDIT_BASE_URL, "/r/CryptoCurrency/top.json", String.format("?sort=new&limit=%d", n));
        List<RedditPost> posts = readRedditPostsFromJsonTree(redditPostRoot);

        service.saveAllPosts(posts);
    }

}
