package tech.peterj.coinpamp.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import tech.peterj.coinpamp.model.Coin;
import tech.peterj.coinpamp.model.CoinPrice;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@Service
public class CoinStatsFetcher {

    private static final Logger LOGGER = Logger.getLogger(RedditFetcher.class.getName());
    private static final String COINSTATS_BASE_URL = "https://api.coinstats.app/public/v1";

    private final CoinService service;

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

    private List<CoinPrice> readCoinPriceHistoryFromJsonTree(JsonNode root) {
        Objects.requireNonNull(root);

        var chartNode = root.get("chart");
        var prices = new ArrayList<CoinPrice>();

        chartNode.forEach(priceNode -> prices.add(new CoinPrice(
                priceNode.get(0).asLong(),
                priceNode.get(1).asDouble()
        )));

        LOGGER.info("fetched coin price data");

        return prices;
    }

    @Transactional
    public void fetchCoinPriceDataForToday() throws IOException, InterruptedException {
        JsonNode coinRoot = Fetcher.fetch(COINSTATS_BASE_URL, "/coins/bitcoin", "?currency=USD");
        JsonNode pricesRoot = Fetcher.fetch(COINSTATS_BASE_URL, "/charts", "?period=24h&coinId=bitcoin");

        Coin coin = readCoinDataFromJsonTree(coinRoot);
        coin.setPrices(readCoinPriceHistoryFromJsonTree(pricesRoot));

        service.saveCoin(coin);
    }

}
