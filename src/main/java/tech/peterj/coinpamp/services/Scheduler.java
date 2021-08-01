package tech.peterj.coinpamp.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.logging.Logger;

@Service
public class Scheduler {

    private static final Logger LOGGER = Logger.getLogger(Scheduler.class.getName());

    private final CoinStatsFetcher coinStatsFetcher;

    public Scheduler(CoinStatsFetcher coinStatsFetcher) {
        this.coinStatsFetcher = coinStatsFetcher;
    }

    @Scheduled(fixedDelay = 300000) // fetch every 5 minutes: fixedDelay = 300000
    public void doScheduledUpdate() throws IOException, InterruptedException {
        LOGGER.info("performing scheduled update ...");
        coinStatsFetcher.fetchCoinPriceDataForToday();
    }

}
