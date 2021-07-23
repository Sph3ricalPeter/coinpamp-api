package tech.peterj.coinpamp.services;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import tech.peterj.coinpamp.model.Coin;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
class CoinServiceTest {

    @Autowired
    private CoinService coinService;

    @Test
    public void testCoinService_saveNewCoin_findCoinSuccessfully() {
        Coin expected = new Coin(
                "bitcoin",
                "Bitcoin",
                "BTC",
                null
        );
        coinService.saveCoin(expected);

        Coin actual = coinService.findCoin("bitcoin");
        assertNotNull(actual);
    }

}