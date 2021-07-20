package tech.peterj.coinpamp.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import tech.peterj.coinpamp.model.Coin;
import tech.peterj.coinpamp.model.CoinPrice;

import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@Service
public class CoinStatsFetcher {

    private static final Logger LOGGER = Logger.getLogger(RedditFetcher.class.getName());
    private static final String COINSTATS_BASE_URL = "https://api.coinstats.app/public/v1";

    private CoinService service;

    public CoinStatsFetcher(CoinService service) {
        this.service = service;
    }

    private Coin readCoinDataFromJsonTree(JsonNode root) {
        Objects.requireNonNull(root);

        var coinNode = root.get("coin");
        var coin = new Coin(
                coinNode.get("id").asText(),
                coinNode.get("name").asText(),
                coinNode.get("symbol").asText(),
                new ArrayList<>()
        );

        LOGGER.info("fetched " + coin.getName() + " data");

        return coin;
    }

    private List<CoinPrice> readCoinPricesFromJsonNode(JsonNode root) {
        Objects.requireNonNull(root);

        var chartNode = root.get("chart");
        var prices = new ArrayList<CoinPrice>();

        chartNode.forEach(priceNode -> prices.add(new CoinPrice(
                priceNode.get(1).asDouble(),
                Timestamp.from(Instant.ofEpochSecond(priceNode.get(0).asLong()))
        )));

        LOGGER.info("fetched coin price data");

        return prices;
    }

    // @Scheduled(fixedDelay = 1000) // fetch every 5 minutes: fixedDelay = 300000
    public void doScheduledFetch() {
        LOGGER.info("fetching ...");
    }

    @Transactional
    public void fetchCoinPriceDataForOneMonth() throws IOException, InterruptedException {
        JsonNode coinRoot = Fetcher.fetch(COINSTATS_BASE_URL, "/coins/bitcoin", "?currency=USD");
        JsonNode pricesRoot = Fetcher.fetch(COINSTATS_BASE_URL, "/charts", "?period=1m&coinId=bitcoin");

        Coin coin = readCoinDataFromJsonTree(coinRoot);
        coin.setPrices(readCoinPricesFromJsonNode(pricesRoot));

        service.saveCoin(coin);
    }

}
