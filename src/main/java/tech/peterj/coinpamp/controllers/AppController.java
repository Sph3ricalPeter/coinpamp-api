package tech.peterj.coinpamp.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import tech.peterj.coinpamp.services.RedditFetcher;

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
    private final RedditFetcher fetcher;

    public AppController(RedditFetcher fetcher) {
        this.fetcher = fetcher;
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

}
