package tech.peterj.coinpamp.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import tech.peterj.coinpamp.model.RedditPost;
import tech.peterj.coinpamp.services.RedditFetcher;
import tech.peterj.coinpamp.services.RedditPostService;

import java.io.IOException;
import java.util.logging.Logger;

@RestController
public class AppController {

    private static final Logger LOGGER = Logger.getLogger(AppController.class.getName());
    private final RedditFetcher fetcher;
    private final RedditPostService service;

    public AppController(RedditFetcher fetcher, RedditPostService service) {
        this.fetcher = fetcher;
        this.service = service;
    }

    @GetMapping("/alive")
    public ResponseEntity<String> healthCheck() {
        LOGGER.info("REST request health check");
        return new ResponseEntity<>("{\"status\" : \"UP\"}", HttpStatus.OK);
    }

    @GetMapping("/fetch/{n}")
    public ResponseEntity<String> fetch(@PathVariable int n) throws IOException, InterruptedException {
        LOGGER.info("REST request fetch");
        fetcher.fetchLatestNCryptoPosts(n);
        return new ResponseEntity<>("{\"fetched ...\"}", HttpStatus.OK);
    }

    @GetMapping("/redditThread/{id}")
    public ResponseEntity<RedditPost> getThreadById(@PathVariable String id) {
        RedditPost post = service.findPost(id);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

}
