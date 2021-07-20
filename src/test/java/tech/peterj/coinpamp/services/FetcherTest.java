package tech.peterj.coinpamp.services;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
class FetcherTest {

    @Test
    public void testFetch_validUrl_responseOk_bodyContainsValidJSON() throws IOException, InterruptedException {
        var rootNode = Fetcher.fetch("https://api.coinstats.app/public/v1", "/coins/bitcoin", "?currency=USD");
        assertNotNull(rootNode);
    }

}