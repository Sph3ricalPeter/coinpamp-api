package tech.peterj.coinpamp.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import tech.peterj.coinpamp.model.Coin;
import tech.peterj.coinpamp.model.RedditPost;
import tech.peterj.coinpamp.services.CoinService;
import tech.peterj.coinpamp.services.CoinStatsFetcher;
import tech.peterj.coinpamp.services.RedditFetcher;
import tech.peterj.coinpamp.services.RedditPostService;

import java.io.IOException;
import java.util.logging.Logger;

@RestController
public class AppController {

    private static final Logger LOGGER = Logger.getLogger(AppController.class.getName());
    private final RedditFetcher redditFetcher;
    private final RedditPostService redditPostService;
    private final CoinStatsFetcher coinStatsFetcher;
    private final CoinService coinService;


    public AppController(RedditFetcher fetcher, RedditPostService service, CoinStatsFetcher coinStatsFetcher, CoinService coinService) {
        this.redditFetcher = fetcher;
        this.redditPostService = service;
        this.coinStatsFetcher = coinStatsFetcher;
        this.coinService = coinService;
    }

    @GetMapping("/alive")
    public ResponseEntity<String> healthCheck() {
        LOGGER.info("REST request health check");
        return new ResponseEntity<>("{\"status\" : \"UP\"}", HttpStatus.OK);
    }

    @GetMapping("/fetch/{n}")
    public ResponseEntity<String> fetch(@PathVariable int n) throws IOException, InterruptedException {
        LOGGER.info("REST request fetch");
        redditFetcher.fetchLatestNCryptoPosts(n);
        return new ResponseEntity<>("{\"fetched ...\"}", HttpStatus.OK);
    }

    @GetMapping("/redditThread/{id}")
    public ResponseEntity<RedditPost> getThreadById(@PathVariable String id) {
        RedditPost post = redditPostService.findPost(id);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }


    @GetMapping("/fetchCoins")
    public ResponseEntity<String> fetchCoins() throws IOException, InterruptedException {
        LOGGER.info("REST request fetch coins");
        coinStatsFetcher.fetchCoinPriceDataForOneMonth();
        return new ResponseEntity<>("{\"fetched coins...\"}", HttpStatus.OK);
    }

    @GetMapping("/coins/{id}")
    public ResponseEntity<Coin> getCoinById(@PathVariable String id) {
        var coin = coinService.findCoin(id);
        return new ResponseEntity<>(coin, HttpStatus.OK);
    }

}
